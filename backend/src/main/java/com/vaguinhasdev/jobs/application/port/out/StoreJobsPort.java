package com.vaguinhasdev.jobs.application.port.out;

import com.vaguinhasdev.jobs.domain.model.Job;
import com.vaguinhasdev.jobs.domain.model.JobSource;

import java.util.Optional;

public interface StoreJobsPort {

    Optional<Job> findBySourceAndExternalId(JobSource source, String externalId);

    Optional<Job> findByFingerprint(String fingerprint);

    Job save(Job job);
}
