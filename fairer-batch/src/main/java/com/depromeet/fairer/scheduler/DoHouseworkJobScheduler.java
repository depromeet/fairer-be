package com.depromeet.fairer.scheduler;

import com.depromeet.fairer.config.DoHouseworkJobConfig;
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
public class DoHouseworkJobScheduler {
    private final JobLauncher jobLauncher;
    private final DoHouseworkJobConfig doHouseworkJobConfig;

    @Scheduled(cron = "0 */10 * * * *")
    public void doHouseworkJobScheduler() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(doHouseworkJobConfig.doHouseworkJob(), jobParameters);
        } catch (Exception e) {
            log.error("[{}] Error.", doHouseworkJobConfig.doHouseworkJob().getName(), e.getMessage());
        }
    }
}
