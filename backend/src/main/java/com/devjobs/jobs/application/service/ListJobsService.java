package com.devjobs.jobs.application.service;

import com.devjobs.jobs.application.port.in.ListJobsUseCase;
import com.devjobs.jobs.application.port.out.LoadJobsPort;
import com.devjobs.jobs.domain.model.Job;

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
