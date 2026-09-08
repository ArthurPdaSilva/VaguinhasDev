package com.vaguinhasdev.jobs.config;

import com.vaguinhasdev.jobs.application.port.in.ListJobsUseCase;
import com.vaguinhasdev.jobs.application.port.out.LoadJobsPort;
import com.vaguinhasdev.jobs.application.service.ListJobsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class JobsConfiguration {

    @Bean
    ListJobsUseCase listJobsUseCase(LoadJobsPort loadJobsPort) {
        return new ListJobsService(loadJobsPort);
    }
}
