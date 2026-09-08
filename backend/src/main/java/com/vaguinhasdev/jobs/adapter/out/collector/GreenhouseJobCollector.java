package com.vaguinhasdev.jobs.adapter.out.collector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.application.port.out.JobCollector;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class GreenhouseJobCollector implements JobCollector {

    private final RestClient restClient;
    private final List<ConfiguredBoard> boards;

    public GreenhouseJobCollector(
            RestClient.Builder restClientBuilder,
            @Value("${collectors.greenhouse.boards:}") String boards
    ) {
        this.restClient = restClientBuilder.clone().baseUrl("https://boards-api.greenhouse.io").build();
        this.boards = ConfiguredBoard.parse(boards);
    }

    @Override
    public JobSource source() {
        return JobSource.GREENHOUSE;
    }

    @Override
    public List<CollectedJob> fetch() {
        List<CollectedJob> collectedJobs = new ArrayList<>();
        for (ConfiguredBoard board : boards) {
            GreenhouseResponse response = restClient.get()
                    .uri("/v1/boards/{board}/jobs?content=true", board.identifier())
                    .retrieve()
                    .body(GreenhouseResponse.class);

            if (response == null || response.jobs() == null) continue;
            response.jobs().stream()
                    .map(job -> map(board, job))
                    .forEach(collectedJobs::add);
        }
        return collectedJobs;
    }

    private CollectedJob map(ConfiguredBoard board, GreenhouseJob job) {
        return new CollectedJob(
                String.valueOf(job.id()), source(), job.absoluteUrl(), board.company(), job.title(),
                job.content(), job.location() == null ? null : job.location().name(), null, null
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GreenhouseResponse(List<GreenhouseJob> jobs) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GreenhouseJob(
            long id,
            String title,
            String content,
            @JsonProperty("absolute_url") String absoluteUrl,
            GreenhouseLocation location
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GreenhouseLocation(String name) {
    }
}
