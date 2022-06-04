package com.depromeet.fairer.global.config;

import com.depromeet.fairer.global.exception.handler.FeignClientExceptionErrorDecoder;
import feign.Logger.Level;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = "com.depromeet")
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    @Bean
    Level feignLoggerLevel() {
        return Level.BASIC;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public FeignClientExceptionErrorDecoder commonFeignErrorDecoder() {
        return new FeignClientExceptionErrorDecoder();
    }
}
