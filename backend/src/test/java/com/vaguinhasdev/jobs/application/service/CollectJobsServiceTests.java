package com.vaguinhasdev.jobs.application.service;

import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.application.port.out.JobCollector;
import com.vaguinhasdev.jobs.application.port.out.StoreJobsPort;
import com.vaguinhasdev.jobs.domain.model.Job;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CollectJobsServiceTests {

    @Test
    void deduplicatesTheSameJobAcrossSourcesByFingerprint() {
        InMemoryStore store = new InMemoryStore();
        JobCollector greenhouse = collector(JobSource.GREENHOUSE, "greenhouse-1");
        JobCollector lever = collector(JobSource.LEVER, "lever-1");
        CollectJobsService service = new CollectJobsService(
                List.of(greenhouse, lever),
                store,
                Clock.fixed(Instant.parse("2026-09-08T12:00:00Z"), ZoneOffset.UTC)
        );

        int processed = service.execute();

        assertThat(processed).isEqualTo(2);
        assertThat(store.jobs).singleElement().satisfies(job -> {
            assertThat(job.source()).isEqualTo(JobSource.GREENHOUSE);
            assertThat(job.externalId()).isEqualTo("greenhouse-1");
        });
    }

    private JobCollector collector(JobSource source, String externalId) {
        return new JobCollector() {
            @Override
            public JobSource source() {
                return source;
            }

            @Override
            public List<CollectedJob> fetch() {
                return List.of(new CollectedJob(
                        externalId, source, "https://example.com/" + externalId, "Acme",
                        "Backend Engineer", "Build APIs", "Remote", "remote", null
                ));
            }
        };
    }

    private static class InMemoryStore implements StoreJobsPort {

        private final List<Job> jobs = new ArrayList<>();

        @Override
        public Optional<Job> findBySourceAndExternalId(JobSource source, String externalId) {
            return jobs.stream()
                    .filter(job -> job.source() == source && job.externalId().equals(externalId))
                    .findFirst();
        }

        @Override
        public Optional<Job> findByFingerprint(String fingerprint) {
            return jobs.stream().filter(job -> job.fingerprint().equals(fingerprint)).findFirst();
        }

        @Override
        public Job save(Job job) {
            jobs.removeIf(existing -> existing.id().equals(job.id()));
            jobs.add(job);
            return job;
        }
    }
}
