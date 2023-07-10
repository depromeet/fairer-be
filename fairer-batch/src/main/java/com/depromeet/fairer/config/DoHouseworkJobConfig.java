package com.depromeet.fairer.config;

import com.depromeet.fairer.domain.command.*;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DoHouseworkJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final DomainConfigurationProperties properties;
    private static final RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();

    @Bean
    public Job doHouseworkJob() {
        return jobBuilderFactory.get("DoHouseworkJob")
                .start(doHouseworkStep())
                .preventRestart()
                .build();
    }

    @Bean
    @JobScope
    public Step doHouseworkStep() {
        return stepBuilderFactory.get("DoHouseworkStep")
                .<DoHouseworkCommand, DoHouseworkCommand>chunk(1)
                .reader(doHouseworkReader())
                .writer(doHouseworkWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<DoHouseworkCommand> doHouseworkReader() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime base = now.minusMinutes(now.getMinute()%10);
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String weekly = now.getDayOfWeek().toString();
        String month = Integer.toString(now.getDayOfMonth());
        String time = base.format(DateTimeFormatter.ofPattern("HH:mm:00"));
        return new JdbcCursorItemReaderBuilder<DoHouseworkCommand>()
                .name("DoHouseworkReader")
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(DoHouseworkCommand.class))
                .sql("SELECT assignment.member_id as memberId, housework.housework_name as houseworkName, housework.scheduled_time as scheduledTime\n" +
                        "FROM housework\n" +
                        "INNER JOIN assignment ON assignment.housework_id=housework.housework_id\n" +
                        "INNER JOIN member ON assignment.member_id=member.member_id\n" +
                        "INNER JOIN alarm ON alarm.member_id=member.member_id\n" +
                        "WHERE member.fcm_token IS NOT NULL AND alarm.scheduled_time_status=1\n" +
                        "AND housework.scheduled_date <= ? AND (housework.repeat_end_date IS NULL OR ?<=housework.repeat_end_date)\n" +
                        "AND ((housework.repeat_cycle='ONCE' AND housework.repeat_pattern=?)\n" +
                        "OR (housework.repeat_cycle='WEEKLY' AND INSTR(housework.repeat_pattern, ?)>0)\n" +
                        "OR (housework.repeat_cycle='MONTHLY' AND housework.repeat_pattern=?))\n" +
                        "AND housework.scheduled_time=?")
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{date, date, date, weekly, month, time}))
                .saveState(false)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<DoHouseworkCommand> doHouseworkWriter() {
        return items -> {
            for(DoHouseworkCommand item : items) {
                String uri = UriComponentsBuilder.fromHttpUrl(properties.getApiUrl())
                        .path("/api/fcm/message")
                        .encode().build().toString();

                FCMMessageResponse response = restTemplate.postForObject(uri, getFCMMessageRequest(item), FCMMessageResponse.class);
            }
        };
    }

    private FCMMessageRequest getFCMMessageRequest(DoHouseworkCommand command) {

        log.info("push test!!!!!!!!!!!!");
        log.info("houseworkName ::::::::::::::::::: " + command.getHouseworkName());

        Long memberId = command.getMemberId();
        String title = String.format(FCMMessageTemplate.DO_HOUSEWORK.getTitle(), command.getHouseworkName());
        String body = String.format(FCMMessageTemplate.DO_HOUSEWORK.getBody(), command.getScheduledTime().format(DateTimeFormatter.ofPattern("a hh시 mm분")), command.getScheduledTime().get(ChronoField.CLOCK_HOUR_OF_AMPM), command.getScheduledTime().get(ChronoField.MINUTE_OF_HOUR));
        return FCMMessageRequest.of(memberId, title, body);
    }
}
