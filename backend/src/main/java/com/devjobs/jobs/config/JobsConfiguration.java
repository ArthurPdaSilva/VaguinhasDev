package com.devjobs.jobs.config;

import com.devjobs.jobs.application.port.in.ListJobsUseCase;
import com.devjobs.jobs.application.port.out.LoadJobsPort;
import com.devjobs.jobs.application.service.ListJobsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class JobsConfiguration {

    @Bean
    ListJobsUseCase listJobsUseCase(LoadJobsPort loadJobsPort) {
        return new ListJobsService(loadJobsPort);
    }
}
