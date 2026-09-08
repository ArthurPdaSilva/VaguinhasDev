package com.vaguinhasdev.jobs.application.service;

import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.domain.model.Job;
import com.vaguinhasdev.jobs.domain.model.JobStatus;
import com.vaguinhasdev.jobs.domain.model.Seniority;
import com.vaguinhasdev.jobs.domain.model.WorkModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

public class JobNormalizer {

    private final Clock clock;

    public JobNormalizer(Clock clock) {
        this.clock = clock;
    }

    Job normalize(CollectedJob collectedJob) {
        Instant now = clock.instant();
        String title = clean(collectedJob.title());
        String company = clean(collectedJob.company());
        String location = clean(collectedJob.location());

        return new Job(
                UUID.randomUUID(),
                collectedJob.externalId(),
                collectedJob.source(),
                collectedJob.sourceUrl(),
                company,
                title,
                cleanDescription(collectedJob.description()),
                seniorityOf(title),
                workModelOf(collectedJob.workplaceType(), title, location),
                location.isBlank() ? "Não informado" : location,
                collectedJob.publishedAt(),
                now,
                now,
                JobStatus.ACTIVE,
                fingerprint(company, title, location)
        );
    }

    private Seniority seniorityOf(String title) {
        String value = normalizeText(title);
        if (containsAny(value, "intern", "internship", "estagio", "estagiario")) return Seniority.INTERN;
        if (containsAny(value, "junior", " jr ", "jr.")) return Seniority.JUNIOR;
        if (containsAny(value, "principal")) return Seniority.PRINCIPAL;
        if (containsAny(value, "staff")) return Seniority.STAFF;
        if (containsAny(value, "senior", " sr ", "sr.")) return Seniority.SENIOR;
        if (containsAny(value, "mid level", "mid-level", "pleno")) return Seniority.MID_LEVEL;
        return Seniority.UNKNOWN;
    }

    private WorkModel workModelOf(String workplaceType, String title, String location) {
        String value = normalizeText(String.join(" ", valueOrEmpty(workplaceType), title, location));
        if (containsAny(value, "hybrid", "hibrido")) return WorkModel.HYBRID;
        if (containsAny(value, "remote", "remoto", "anywhere")) return WorkModel.REMOTE;
        if (containsAny(value, "on-site", "onsite", "presencial")) return WorkModel.ONSITE;
        return WorkModel.UNKNOWN;
    }

    private String fingerprint(String company, String title, String location) {
        String value = normalizeText(company) + '|' + normalizeText(title) + '|' + normalizeText(location);
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 não está disponível", exception);
        }
    }

    private String cleanDescription(String value) {
        return clean(value)
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replaceAll("<[^>]+>", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String clean(String value) {
        return valueOrEmpty(value).trim();
    }

    private String normalizeText(String value) {
        return java.text.Normalizer.normalize(valueOrEmpty(value), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean containsAny(String value, String... terms) {
        for (String term : terms) {
            if (value.contains(term)) return true;
        }
        return false;
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
