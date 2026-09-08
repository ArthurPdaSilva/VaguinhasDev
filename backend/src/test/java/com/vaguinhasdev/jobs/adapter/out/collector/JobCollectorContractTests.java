package com.vaguinhasdev.jobs.adapter.out.collector;

import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class JobCollectorContractTests {

    @Test
    void mapsGreenhouseJobs() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://boards-api.greenhouse.io/v1/boards/acme/jobs?content=true"))
                .andRespond(withSuccess("""
                        {"jobs":[{"id":123,"title":"Senior Java Engineer","content":"Build APIs",
                        "absolute_url":"https://boards.greenhouse.io/acme/jobs/123",
                        "location":{"name":"Remote"}}]}
                        """, MediaType.APPLICATION_JSON));

        List<CollectedJob> jobs = new GreenhouseJobCollector(builder, "Acme=acme").fetch();

        assertThat(jobs).singleElement().satisfies(job -> {
            assertThat(job.externalId()).isEqualTo("123");
            assertThat(job.source()).isEqualTo(JobSource.GREENHOUSE);
            assertThat(job.company()).isEqualTo("Acme");
            assertThat(job.sourceUrl()).isEqualTo("https://boards.greenhouse.io/acme/jobs/123");
        });
        server.verify();
    }

    @Test
    void mapsLeverJobs() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.lever.co/v0/postings/acme?mode=json"))
                .andRespond(withSuccess("""
                        [{"id":"job-1","text":"Backend Engineer","descriptionPlain":"Build APIs",
                        "hostedUrl":"https://jobs.lever.co/acme/job-1","workplaceType":"hybrid",
                        "categories":{"location":"São Paulo"}}]
                        """, MediaType.APPLICATION_JSON));

        List<CollectedJob> jobs = new LeverJobCollector(builder, "Acme=acme").fetch();

        assertThat(jobs).singleElement().satisfies(job -> {
            assertThat(job.externalId()).isEqualTo("job-1");
            assertThat(job.source()).isEqualTo(JobSource.LEVER);
            assertThat(job.workplaceType()).isEqualTo("hybrid");
            assertThat(job.location()).isEqualTo("São Paulo");
        });
        server.verify();
    }

    @Test
    void mapsOnlyListedAshbyJobs() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.ashbyhq.com/posting-api/job-board/acme"))
                .andRespond(withSuccess("""
                        {"jobs":[
                        {"title":"Platform Engineer","location":"Remote","workplaceType":"Remote",
                        "descriptionPlain":"Build platforms","publishedAt":"2026-09-01T12:00:00Z",
                        "jobUrl":"https://jobs.ashbyhq.com/acme/job-1","isListed":true},
                        {"title":"Hidden","jobUrl":"https://jobs.ashbyhq.com/acme/hidden","isListed":false}
                        ]}
                        """, MediaType.APPLICATION_JSON));

        List<CollectedJob> jobs = new AshbyJobCollector(builder, "Acme=acme").fetch();

        assertThat(jobs).singleElement().satisfies(job -> {
            assertThat(job.externalId()).isEqualTo("https://jobs.ashbyhq.com/acme/job-1");
            assertThat(job.source()).isEqualTo(JobSource.ASHBY);
            assertThat(job.publishedAt()).isEqualTo(Instant.parse("2026-09-01T12:00:00Z"));
        });
        server.verify();
    }
}
