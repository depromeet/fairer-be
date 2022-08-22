package com.depromeet.fairer.config;

import com.depromeet.fairer.domain.command.FCMMessageRequest;
import com.depromeet.fairer.domain.command.FCMMessageResponse;
import com.depromeet.fairer.domain.command.FCMMessageTemplate;
import com.depromeet.fairer.domain.command.InduceAddHouseworkCommand;
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
public class InduceAddHouseworkJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final DomainConfigurationProperties properties;
    private static final RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();

    @Bean
    public Job induceAddHouseworkJob() {
        return jobBuilderFactory.get("InduceAddHouseworkJob")
                .start(induceAddHouseworkStep())
                .preventRestart()
                .build();
    }

    @Bean
    @JobScope
    public Step induceAddHouseworkStep() {
        return stepBuilderFactory.get("InduceAddHouseworkStep")
                .<InduceAddHouseworkCommand, InduceAddHouseworkCommand>chunk(1)
                .reader(induceAddHouseworkReader())
                .writer(induceAddHouseworkWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<InduceAddHouseworkCommand> induceAddHouseworkReader() {
        return new JdbcCursorItemReaderBuilder<InduceAddHouseworkCommand>()
                .name("InduceAddHouseworkReader")
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(InduceAddHouseworkCommand.class))
                .sql("SELECT member_id, team_name\n" +
                        "FROM member INNER JOIN team ON team.team_id=member.team_id\n" +
                        "WHERE member.fcm_token IS NOT NULL")
                .saveState(false)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<InduceAddHouseworkCommand> induceAddHouseworkWriter() {
        return items -> {
            for(InduceAddHouseworkCommand item : items) {
                String uri = UriComponentsBuilder.fromHttpUrl(properties.getApiUrl())
                        .path("/api/fcm/message")
                        .encode().build().toString();

                FCMMessageResponse response = restTemplate.postForObject(uri, getFCMMessageRequest(item), FCMMessageResponse.class);
            }
        };
    }

    private FCMMessageRequest getFCMMessageRequest(InduceAddHouseworkCommand command) {
        Long memberId = command.getMemberId();
        String title = FCMMessageTemplate.INDUCE_ADD_HOUSEWORK.getTitle();
        String body = String.format(FCMMessageTemplate.INDUCE_ADD_HOUSEWORK.getBody(), command.getTeamName());
        return FCMMessageRequest.of(memberId, title, body);
    }
}
