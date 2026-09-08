package com.vaguinhasdev.jobs.application.port.out;

import com.vaguinhasdev.jobs.application.model.CollectedJob;
import com.vaguinhasdev.jobs.domain.model.JobSource;

import java.util.List;

public interface JobCollector {

    JobSource source();

    List<CollectedJob> fetch();
}
