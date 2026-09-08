package com.vaguinhasdev.jobs.adapter.out.persistence;

import com.vaguinhasdev.jobs.application.port.out.LoadJobsPort;
import com.vaguinhasdev.jobs.domain.model.Job;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
class JpaJobQueryAdapter implements LoadJobsPort {

    private final SpringDataJobRepository repository;

    JpaJobQueryAdapter(SpringDataJobRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> loadAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "publishedAt"))
                .stream()
                .map(JpaJobEntity::toDomain)
                .toList();
    }
}
