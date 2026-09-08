package com.vaguinhasdev.jobs.application.service;

import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import com.vaguinhasdev.jobs.domain.model.Seniority;
import com.vaguinhasdev.jobs.domain.model.WorkModel;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class JobNormalizerTests {

    private static final Instant NOW = Instant.parse("2026-09-08T12:00:00Z");
    private final JobNormalizer normalizer = new JobNormalizer(Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void normalizesCollectedJob() {
        CollectedJob collectedJob = new CollectedJob(
                "external-1",
                JobSource.GREENHOUSE,
                "https://example.com/job",
                " Acme ",
                " Pessoa Desenvolvedora Sênior ",
                "&lt;p&gt;Construa &amp; mantenha APIs&lt;/p&gt;",
                "Remoto - Brasil",
                null,
                null
        );

        var job = normalizer.normalize(collectedJob);

        assertThat(job.company()).isEqualTo("Acme");
        assertThat(job.title()).isEqualTo("Pessoa Desenvolvedora Sênior");
        assertThat(job.description()).isEqualTo("Construa & mantenha APIs");
        assertThat(job.seniority()).isEqualTo(Seniority.SENIOR);
        assertThat(job.workModel()).isEqualTo(WorkModel.REMOTE);
        assertThat(job.discoveredAt()).isEqualTo(NOW);
        assertThat(job.fingerprint()).hasSize(64);
    }

    @Test
    void usesExplicitWorkplaceType() {
        CollectedJob collectedJob = new CollectedJob(
                "external-2", JobSource.LEVER, "https://example.com/job", "Acme",
                "Backend Engineer", "Description", "São Paulo", "hybrid", NOW
        );

        assertThat(normalizer.normalize(collectedJob).workModel()).isEqualTo(WorkModel.HYBRID);
    }
}
