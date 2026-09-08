package com.vaguinhasdev.jobs.config;

import com.vaguinhasdev.jobs.application.port.in.CollectJobsUseCase;
import com.vaguinhasdev.jobs.application.port.in.ListJobsUseCase;
import com.vaguinhasdev.jobs.application.port.out.JobCollector;
import com.vaguinhasdev.jobs.application.port.out.LoadJobsPort;
import com.vaguinhasdev.jobs.application.port.out.StoreJobsPort;
import com.vaguinhasdev.jobs.application.service.CollectJobsService;
import com.vaguinhasdev.jobs.application.service.ListJobsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

import java.time.Clock;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableScheduling
public class JobsConfiguration {

    @Bean
    ListJobsUseCase listJobsUseCase(LoadJobsPort loadJobsPort) {
        return new ListJobsService(loadJobsPort);
    }

    @Bean
    CollectJobsUseCase collectJobsUseCase(
            List<JobCollector> collectors,
            StoreJobsPort storeJobsPort,
            Clock clock
    ) {
        return new CollectJobsService(collectors, storeJobsPort, clock);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
