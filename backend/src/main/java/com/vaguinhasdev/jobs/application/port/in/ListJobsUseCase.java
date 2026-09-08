package com.vaguinhasdev.jobs.application.port.in;

import com.vaguinhasdev.jobs.domain.model.Job;

import java.util.List;

public interface ListJobsUseCase {

    List<Job> execute();
}
