package com.vaguinhasdev.jobs.application.port.out;

import com.vaguinhasdev.jobs.domain.model.Job;

import java.util.List;

public interface LoadJobsPort {

    List<Job> loadAll();
}
