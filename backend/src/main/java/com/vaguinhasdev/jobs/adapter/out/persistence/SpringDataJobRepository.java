package com.vaguinhasdev.jobs.adapter.out.persistence;

import com.vaguinhasdev.jobs.domain.model.JobSource;
import com.vaguinhasdev.jobs.domain.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataJobRepository extends JpaRepository<JpaJobEntity, UUID> {

    List<JpaJobEntity> findAllByStatus(JobStatus status, Sort sort);

    Optional<JpaJobEntity> findBySourceAndExternalId(JobSource source, String externalId);

    Optional<JpaJobEntity> findByFingerprint(String fingerprint);
}
