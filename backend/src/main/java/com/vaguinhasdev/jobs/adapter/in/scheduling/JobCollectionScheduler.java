package com.vaguinhasdev.jobs.adapter.in.scheduling;

import com.vaguinhasdev.jobs.application.port.in.CollectJobsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class JobCollectionScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCollectionScheduler.class);

    private final CollectJobsUseCase collectJobs;
    private final boolean enabled;

    JobCollectionScheduler(
            CollectJobsUseCase collectJobs,
            @Value("${collectors.enabled:true}") boolean enabled
    ) {
        this.collectJobs = collectJobs;
        this.enabled = enabled;
    }

    @Scheduled(
            initialDelayString = "${collectors.initial-delay:PT5S}",
            fixedDelayString = "${collectors.fixed-delay:PT1H}"
    )
    void collect() {
        if (!enabled) return;

        try {
            int collectedJobs = collectJobs.execute();
            LOGGER.info("Coleta finalizada: {} vagas processadas", collectedJobs);
        } catch (RuntimeException exception) {
            LOGGER.error("Falha durante a coleta de vagas", exception);
        }
    }
}
