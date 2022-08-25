package com.depromeet.fairer.scheduler;

import com.depromeet.fairer.config.InduceAddHouseworkJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InduceAddHouseworkJobScheduler {
    private final JobLauncher jobLauncher;
    private final InduceAddHouseworkJobConfig induceAddHouseworkJobConfig;

    @Scheduled(cron = "0 0 9 * * *")
    public void induceAddHouseworkJobScheduler() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(induceAddHouseworkJobConfig.induceAddHouseworkJob(), jobParameters);
        } catch (Exception e) {
            log.error("[{}] Error.", induceAddHouseworkJobConfig.induceAddHouseworkJob().getName(), e.getMessage());
        }
    }
}
