package com.vaguinhasdev.jobs.application.service;

import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.application.port.in.CollectJobsUseCase;
import com.vaguinhasdev.jobs.application.port.out.JobCollector;
import com.vaguinhasdev.jobs.application.port.out.StoreJobsPort;
import com.vaguinhasdev.jobs.domain.model.Job;
import com.vaguinhasdev.jobs.domain.model.JobStatus;

import java.time.Clock;
import java.util.List;

public class CollectJobsService implements CollectJobsUseCase {

    private final List<JobCollector> collectors;
    private final StoreJobsPort storeJobsPort;
    private final JobNormalizer normalizer;
    private final Clock clock;

    public CollectJobsService(List<JobCollector> collectors, StoreJobsPort storeJobsPort, Clock clock) {
        this.collectors = List.copyOf(collectors);
        this.storeJobsPort = storeJobsPort;
        this.normalizer = new JobNormalizer(clock);
        this.clock = clock;
    }

    @Override
    public int execute() {
        int persistedJobs = 0;
        for (JobCollector collector : collectors) {
            for (CollectedJob collectedJob : collector.fetch()) {
                persist(normalizer.normalize(collectedJob));
                persistedJobs++;
            }
        }
        return persistedJobs;
    }

    private void persist(Job normalizedJob) {
        Job job = storeJobsPort
                .findBySourceAndExternalId(normalizedJob.source(), normalizedJob.externalId())
                .map(existing -> refresh(existing, normalizedJob))
                .orElseGet(() -> storeJobsPort.findByFingerprint(normalizedJob.fingerprint())
                        .map(this::refreshDuplicate)
                        .orElse(normalizedJob));

        storeJobsPort.save(job);
    }

    private Job refresh(Job existing, Job normalized) {
        return new Job(
                existing.id(), normalized.externalId(), normalized.source(), normalized.sourceUrl(),
                normalized.company(), normalized.title(), normalized.description(), normalized.seniority(),
                normalized.workModel(), normalized.location(), normalized.publishedAt(), existing.discoveredAt(),
                clock.instant(), JobStatus.ACTIVE, normalized.fingerprint()
        );
    }

    private Job refreshDuplicate(Job existing) {
        return new Job(
                existing.id(), existing.externalId(), existing.source(), existing.sourceUrl(), existing.company(),
                existing.title(), existing.description(), existing.seniority(), existing.workModel(), existing.location(),
                existing.publishedAt(), existing.discoveredAt(), clock.instant(), JobStatus.ACTIVE, existing.fingerprint()
        );
    }
}
