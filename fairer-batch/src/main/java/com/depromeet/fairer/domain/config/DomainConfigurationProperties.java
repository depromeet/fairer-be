package com.depromeet.fairer.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "domain")
public class DomainConfigurationProperties {
    private String apiUrl;
}
