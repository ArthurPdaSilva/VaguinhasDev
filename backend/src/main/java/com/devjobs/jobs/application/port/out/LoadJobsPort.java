package com.devjobs.jobs.application.port.out;

import com.devjobs.jobs.domain.model.Job;

import java.util.List;

public interface LoadJobsPort {

    List<Job> loadAll();
}
