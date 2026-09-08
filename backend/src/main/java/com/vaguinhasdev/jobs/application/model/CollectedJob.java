package com.vaguinhasdev.jobs.application.model;

import com.vaguinhasdev.jobs.domain.model.JobSource;

import java.time.Instant;

public record CollectedJob(
        String externalId,
        JobSource source,
        String sourceUrl,
        String company,
        String title,
        String description,
        String location,
        String workplaceType,
        Instant publishedAt
) {
}
