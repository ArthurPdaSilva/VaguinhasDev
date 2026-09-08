package com.vaguinhasdev.jobs.adapter.out.collector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.application.port.out.JobCollector;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class AshbyJobCollector implements JobCollector {

    private final RestClient restClient;
    private final List<ConfiguredBoard> boards;

    public AshbyJobCollector(
            RestClient.Builder restClientBuilder,
            @Value("${collectors.ashby.boards:}") String boards
    ) {
        this.restClient = restClientBuilder.clone().baseUrl("https://api.ashbyhq.com").build();
        this.boards = ConfiguredBoard.parse(boards);
    }

    @Override
    public JobSource source() {
        return JobSource.ASHBY;
    }

    @Override
    public List<CollectedJob> fetch() {
        List<CollectedJob> collectedJobs = new ArrayList<>();
        for (ConfiguredBoard board : boards) {
            AshbyResponse response = restClient.get()
                    .uri("/posting-api/job-board/{board}", board.identifier())
                    .retrieve()
                    .body(AshbyResponse.class);

            if (response == null || response.jobs() == null) continue;
            response.jobs().stream()
                    .filter(job -> job.isListed() == null || job.isListed())
                    .map(job -> map(board, job))
                    .forEach(collectedJobs::add);
        }
        return collectedJobs;
    }

    private CollectedJob map(ConfiguredBoard board, AshbyJob job) {
        return new CollectedJob(
                job.jobUrl(), source(), job.jobUrl(), board.company(), job.title(), job.descriptionPlain(),
                job.location(), job.workplaceType(), parseInstant(job.publishedAt())
        );
    }

    private Instant parseInstant(String value) {
        return value == null || value.isBlank() ? null : Instant.parse(value);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record AshbyResponse(List<AshbyJob> jobs) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record AshbyJob(
            String title,
            String location,
            String workplaceType,
            String descriptionPlain,
            String publishedAt,
            String jobUrl,
            Boolean isListed
    ) {
    }
}
