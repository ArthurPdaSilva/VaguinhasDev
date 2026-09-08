package com.vaguinhasdev.jobs.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Job(
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
        JobStatus status,
        String fingerprint
) {
}
