package com.depromeet.fairer.config;

import com.depromeet.fairer.domain.command.FCMMessageRequest;
import com.depromeet.fairer.domain.command.FCMMessageResponse;
import com.depromeet.fairer.domain.command.FCMMessageTemplate;
import com.depromeet.fairer.domain.command.NotCompleteHouseworkRemindCommand;
import com.depromeet.fairer.domain.config.DomainConfigurationProperties;
import com.depromeet.fairer.domain.config.RestTemplateFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotCompleteHouseworkRemindJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final DomainConfigurationProperties properties;
    private static final RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();

    @Bean
    public Job notCompleteHouseworkRemindJob() {
        return jobBuilderFactory.get("NotCompleteHouseworkRemindJob")
                .start(notCompleteHouseworkRemindStep())
                .preventRestart()
                .build();
    }

    @Bean
    @JobScope
    public Step notCompleteHouseworkRemindStep() {
        return stepBuilderFactory.get("NotCompleteHouseworkRemindStep")
                .<NotCompleteHouseworkRemindCommand, NotCompleteHouseworkRemindCommand>chunk(1)
                .reader(notCompleteHouseworkRemindReader())
                .writer(notCompleteHouseworkRemindWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<NotCompleteHouseworkRemindCommand> notCompleteHouseworkRemindReader() {
        return new JdbcCursorItemReaderBuilder<NotCompleteHouseworkRemindCommand>()
                .name("InduceAddHouseworkReader")
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(NotCompleteHouseworkRemindCommand.class))
                .sql("SELECT member_id as memberId,count(*) as totalCount, housework_name as houseworkName\n" +
                        "FROM assignment INNER JOIN housework ON assignment.housework_id=housework.housework_id\n" +
                        "WHERE success=0 GROUP BY member_id")
                .saveState(false)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<NotCompleteHouseworkRemindCommand> notCompleteHouseworkRemindWriter() {
        return items -> {
            for(NotCompleteHouseworkRemindCommand item : items) {
                String uri = UriComponentsBuilder.fromHttpUrl(properties.getApiUrl())
                        .path("/api/fcm/message")
                        .encode().build().toString();

                FCMMessageResponse response = restTemplate.postForObject(uri, getFCMMessageRequest(item), FCMMessageResponse.class);
            }
        };
    }

    private FCMMessageRequest getFCMMessageRequest(NotCompleteHouseworkRemindCommand command) {
        Long memberId = command.getMemberId();
        String title = String.format(FCMMessageTemplate.NOT_COMPLETE_HOUSEWORK.getTitle(), command.getTotalCount());
        String body = String.format(FCMMessageTemplate.NOT_COMPLETE_HOUSEWORK.getBody(), command.getHouseworkName());
        return FCMMessageRequest.of(memberId, title, body);
    }
}
