package com.vaguinhasdev.jobs.adapter.out.persistence;

import com.vaguinhasdev.jobs.application.port.out.LoadJobsPort;
import com.vaguinhasdev.jobs.application.port.out.StoreJobsPort;
import com.vaguinhasdev.jobs.domain.model.Job;
import com.vaguinhasdev.jobs.domain.model.JobSource;
import com.vaguinhasdev.jobs.domain.model.JobStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
class JpaJobQueryAdapter implements LoadJobsPort, StoreJobsPort {

    private final SpringDataJobRepository repository;

    JpaJobQueryAdapter(SpringDataJobRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> loadAll() {
        return repository.findAllByStatus(JobStatus.ACTIVE, Sort.by(Sort.Direction.DESC, "discoveredAt"))
                .stream()
                .map(JpaJobEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Job> findBySourceAndExternalId(JobSource source, String externalId) {
        return repository.findBySourceAndExternalId(source, externalId).map(JpaJobEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Job> findByFingerprint(String fingerprint) {
        return repository.findByFingerprint(fingerprint).map(JpaJobEntity::toDomain);
    }

    @Override
    @Transactional
    public Job save(Job job) {
        JpaJobEntity entity = repository.findById(job.id()).orElseGet(() -> JpaJobEntity.fromDomain(job));
        entity.update(job);
        return repository.save(entity).toDomain();
    }
}
