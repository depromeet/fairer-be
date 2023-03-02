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
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        LocalDate now = LocalDate.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String weekly = now.getDayOfWeek().toString();
        String month = Integer.toString(now.getDayOfMonth());

        return new JdbcCursorItemReaderBuilder<NotCompleteHouseworkRemindCommand>()
                .name("NotCompleteHouseworkRemindReader")
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(NotCompleteHouseworkRemindCommand.class))
                .sql("SELECT assignment.member_id as memberId, COUNT(*) AS totalCount, housework.housework_name as houseworkName\n" +
                        "FROM housework\n" +
                        "INNER JOIN assignment ON assignment.housework_id=housework.housework_id\n" +
                        "INNER JOIN member ON assignment.member_id=member.member_id\n" +
                        "INNER JOIN alarm ON alarm.member_id=member.member_id\n" +
                        "LEFT OUTER JOIN housework_complete ON housework_complete.housework_id=housework.housework_id\n" +
                        "WHERE member.fcm_token IS NOT NULL AND alarm.not_complete_status=1\n" +
                        "AND housework.scheduled_date <= ? AND (housework.repeat_end_date IS NULL OR ?<=housework.repeat_end_date)\n" +
                        "AND housework_complete.housework_complete_id IS NULL\n" +
                        "AND ((housework.repeat_cycle='ONCE' AND housework.repeat_pattern=?)\n" +
                        "OR (housework.repeat_cycle='WEEKLY' AND INSTR(housework.repeat_pattern, ?)>0)\n" +
                        "OR (housework.repeat_cycle='MONTHLY' AND housework.repeat_pattern=?))\n" +
                        "GROUP BY assignment.member_id")
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{date, date, date, weekly, month}))
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
