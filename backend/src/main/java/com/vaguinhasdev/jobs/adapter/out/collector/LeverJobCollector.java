package com.vaguinhasdev.jobs.adapter.out.collector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.application.port.out.JobCollector;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class LeverJobCollector implements JobCollector {

    private final RestClient restClient;
    private final List<ConfiguredBoard> boards;

    public LeverJobCollector(
            RestClient.Builder restClientBuilder,
            @Value("${collectors.lever.boards:}") String boards
    ) {
        this.restClient = restClientBuilder.clone().baseUrl("https://api.lever.co").build();
        this.boards = ConfiguredBoard.parse(boards);
    }

    @Override
    public JobSource source() {
        return JobSource.LEVER;
    }

    @Override
    public List<CollectedJob> fetch() {
        List<CollectedJob> collectedJobs = new ArrayList<>();
        for (ConfiguredBoard board : boards) {
            List<LeverJob> jobs = restClient.get()
                    .uri("/v0/postings/{site}?mode=json", board.identifier())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (jobs == null) continue;
            jobs.stream().map(job -> map(board, job)).forEach(collectedJobs::add);
        }
        return collectedJobs;
    }

    private CollectedJob map(ConfiguredBoard board, LeverJob job) {
        return new CollectedJob(
                job.id(), source(), job.hostedUrl(), board.company(), job.text(), job.descriptionPlain(),
                job.categories() == null ? null : job.categories().location(), job.workplaceType(), null
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LeverJob(
            String id,
            String text,
            String descriptionPlain,
            String hostedUrl,
            String workplaceType,
            LeverCategories categories
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LeverCategories(String location) {
    }
}
