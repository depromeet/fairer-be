package com.depromeet.fairer.domain.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateFactory {
    public static RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .build();
    }
}
