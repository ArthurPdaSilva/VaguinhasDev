package com.vaguinhasdev.jobs.application.service;

import com.vaguinhasdev.jobs.application.port.in.ListJobsUseCase;
import com.vaguinhasdev.jobs.application.port.out.LoadJobsPort;
import com.vaguinhasdev.jobs.domain.model.Job;

import java.util.List;

public class ListJobsService implements ListJobsUseCase {

    private final LoadJobsPort loadJobsPort;

    public ListJobsService(LoadJobsPort loadJobsPort) {
        this.loadJobsPort = loadJobsPort;
    }

    @Override
    public List<Job> execute() {
        return loadJobsPort.loadAll();
    }
}
