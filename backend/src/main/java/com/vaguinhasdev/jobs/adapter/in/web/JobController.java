package com.vaguinhasdev.jobs.adapter.in.web;

import com.vaguinhasdev.jobs.application.port.in.ListJobsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
class JobController {

    private final ListJobsUseCase listJobs;

    JobController(ListJobsUseCase listJobs) {
        this.listJobs = listJobs;
    }

    @GetMapping
    List<JobResponse> findAll() {
        return listJobs.execute().stream()
                .map(JobResponse::from)
                .toList();
    }
}
