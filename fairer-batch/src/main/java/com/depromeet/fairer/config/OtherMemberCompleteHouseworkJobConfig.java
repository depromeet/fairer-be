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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OtherMemberCompleteHouseworkJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final DomainConfigurationProperties properties;
    private static final RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();

    @Bean
    public Job otherMemberCompleteHouseworkJob() {
        return jobBuilderFactory.get("OtherMemberCompleteHouseworkJob")
                .start(otherMemberCompleteHouseworkStep())
                .preventRestart()
                .build();
    }

    @Bean
    @JobScope
    public Step otherMemberCompleteHouseworkStep() {
        return stepBuilderFactory.get("OtherMemberCompleteHouseworkStep")
                .<OtherMemberCompleteHouseworkCommand, OtherMemberCompleteHouseworkCommand>chunk(1)
                .reader(otherMemberCompleteHouseworkReader())
                .writer(otherMemberCompleteHouseworkWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<OtherMemberCompleteHouseworkCommand> otherMemberCompleteHouseworkReader() {
        LocalDate now = LocalDate.now();
        String date = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String weekly = now.getDayOfWeek().toString();
        String month = Integer.toString(now.getDayOfMonth());
        return new JdbcCursorItemReaderBuilder<OtherMemberCompleteHouseworkCommand>()
                .name("OtherMemberCompleteHouseworkReader")
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(OtherMemberCompleteHouseworkCommand.class))
                .sql("SELECT member1.member_id as member_id, member2.member_name as teamMemberName, COUNT(*) as count\n" +
                        "FROM housework\n" +
                        "INNER JOIN assignment ON assignment.housework_id=housework.housework_id\n" +
                        "INNER JOIN member as member2 ON assignment.member_id=member2.member_id\n" +
                        "LEFT OUTER JOIN housework_complete ON housework_complete.housework_id=housework.housework_id\n" +
                        "RIGHT OUTER JOIN member as member1 ON member1.member_id!=member2.member_id\n" +
                        "WHERE member1.fcm_token IS NOT NULL\n" +
                        "AND housework.team_id=member1.team_id\n" +
                        "AND housework.scheduled_date <= ? AND (housework.repeat_end_date IS NULL OR ?<=housework.repeat_end_date)\n" +
                        "AND ((housework.repeat_cycle='ONCE' AND housework.repeat_pattern=?)\n" +
                        "OR (housework.repeat_cycle='WEEKLY' AND housework.repeat_pattern=?)\n" +
                        "OR (housework.repeat_cycle='MONTHLY' AND housework.repeat_pattern=?))\n" +
                        "GROUP BY member1.member_id, member2.member_id, member2.member_name\n" +
                        "HAVING COUNT(housework.housework_id)>=1 AND COUNT(housework.housework_id)=SUM(!ISNULL(housework_complete.housework_complete_id))")
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{date, date, date, weekly, month}))
                .saveState(false)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<OtherMemberCompleteHouseworkCommand> otherMemberCompleteHouseworkWriter() {
        return items -> {
            for(OtherMemberCompleteHouseworkCommand item : items) {
                String uri = UriComponentsBuilder.fromHttpUrl(properties.getApiUrl())
                        .path("/api/fcm/message")
                        .encode().build().toString();

                FCMMessageResponse response = restTemplate.postForObject(uri, getFCMMessageRequest(item), FCMMessageResponse.class);
            }
        };
    }

    private FCMMessageRequest getFCMMessageRequest(OtherMemberCompleteHouseworkCommand command) {
        Long memberId = command.getMemberId();
        String title = String.format(FCMMessageTemplate.OTHER_MEMBER_COMPLETE_HOUSEWORK.getTitle(), command.getTeamMemberName());
        String body = String.format(FCMMessageTemplate.OTHER_MEMBER_COMPLETE_HOUSEWORK.getBody(), command.getTeamMemberName());
        return FCMMessageRequest.of(memberId, title, body);
    }
}
