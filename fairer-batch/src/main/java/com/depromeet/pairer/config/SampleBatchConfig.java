package com.depromeet.pairer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job sampleJob() {
        return jobBuilderFactory.get("sampleJob")
                .start(sampleStep())
                .preventRestart()
                .build();
    }

    @Bean
    @JobScope
    public Step sampleStep() {
        return stepBuilderFactory.get("sampleStep")
                .<String, String>chunk(1)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<String> itemReader() {
        return new JdbcCursorItemReaderBuilder<String>()
                .name("jdbcCursorItemReader")
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper((rs, rowNum) -> rs.getString(1))
                .sql("SELECT housework_name FROM preset")
                .saveState(false)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<String, String> itemProcessor() {
        return item -> item + " complete";
    }

    @Bean
    @StepScope
    public ItemWriter<String> itemWriter() {
        return items -> {
            for(String item : items) {
                log.info("Result : {}", item);
            }
        };
    }

}
