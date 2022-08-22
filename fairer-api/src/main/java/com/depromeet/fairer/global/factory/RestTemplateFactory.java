package com.depromeet.fairer.global.factory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {
    public static RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .build();
    }
}
