package com.devjobs.jobs.adapter.in.web;

import com.devjobs.jobs.domain.model.Job;
import com.devjobs.jobs.domain.model.JobSource;
import com.devjobs.jobs.domain.model.JobStatus;
import com.devjobs.jobs.domain.model.Seniority;
import com.devjobs.jobs.domain.model.WorkModel;

import java.time.Instant;
import java.util.UUID;

record JobResponse(
        UUID id,
        String externalId,
        JobSource source,
        String sourceUrl,
        String company,
        String title,
        String description,
        Seniority seniority,
        WorkModel workModel,
        String location,
        Instant publishedAt,
        Instant discoveredAt,
        Instant lastSeenAt,
        JobStatus status
) {
    static JobResponse from(Job job) {
        return new JobResponse(job.id(), job.externalId(), job.source(), job.sourceUrl(),
                job.company(), job.title(), job.description(), job.seniority(), job.workModel(),
                job.location(), job.publishedAt(), job.discoveredAt(), job.lastSeenAt(), job.status());
    }
}
