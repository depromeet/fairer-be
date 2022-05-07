package com.depromeet.fairer.global.config;

import com.depromeet.fairer.global.resolver.RequestMemberIdArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArgResolverConfig {

    @Bean
    RequestMemberIdArgumentResolver requestMemberIdArgumentResolver() {
        return new RequestMemberIdArgumentResolver();
    }
}
