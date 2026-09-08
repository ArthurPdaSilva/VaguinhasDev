package com.devjobs.jobs.adapter.out.persistence;

import com.devjobs.jobs.domain.model.Job;
import com.devjobs.jobs.domain.model.JobSource;
import com.devjobs.jobs.domain.model.JobStatus;
import com.devjobs.jobs.domain.model.Seniority;
import com.devjobs.jobs.domain.model.WorkModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jobs")
class JpaJobEntity {

    @Id
    private UUID id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobSource source;

    @Column(name = "source_url", nullable = false, length = 2048)
    private String sourceUrl;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Seniority seniority;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_model", nullable = false)
    private WorkModel workModel;

    @Column(nullable = false)
    private String location;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "discovered_at", nullable = false)
    private Instant discoveredAt;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false, length = 64)
    private String fingerprint;

    protected JpaJobEntity() {
    }

    Job toDomain() {
        return new Job(id, externalId, source, sourceUrl, company, title, description,
                seniority, workModel, location, publishedAt, discoveredAt, lastSeenAt,
                status, fingerprint);
    }
}
