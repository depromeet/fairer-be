package com.depromeet.fairer.scheduler;

import com.depromeet.fairer.config.NotCompleteHouseworkRemindJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotCompleteHouseworkRemindJobScheduler {
    private final JobLauncher jobLauncher;
    private final NotCompleteHouseworkRemindJobConfig notCompleteHouseworkRemindJobConfig;

    @Scheduled(cron = "0 0 21 * * *")
    public void notCompleteHouseworkRemindJobScheduler() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(notCompleteHouseworkRemindJobConfig.notCompleteHouseworkRemindJob(), jobParameters);
        } catch (Exception e) {
            log.error("[{}] Error.", notCompleteHouseworkRemindJobConfig.notCompleteHouseworkRemindJob().getName(), e.getMessage());
        }
    }
}
