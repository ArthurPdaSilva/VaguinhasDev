package com.devjobs.jobs.application.port.in;

import com.devjobs.jobs.domain.model.Job;

import java.util.List;

public interface ListJobsUseCase {

    List<Job> execute();
}
