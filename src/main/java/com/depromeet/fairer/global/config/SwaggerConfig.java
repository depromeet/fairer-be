package com.depromeet.fairer.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Value("${swagger.url}")
    private String url;

    @Value("${swagger.desc}")
    private String desc;

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .ignoredParameterTypes(Errors.class)
                .ignoredParameterTypes(BindingResult.class)
                .servers(getServer(profile, url, desc))
                .groupName("fairer")
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.depromeet.fairer.api"))
                .paths(PathSelectors.ant("/api/**/*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEveryThing");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    /**
     * 인증 버튼 클릭 했을 때 입력하는 값을 넣는 설정
     */
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    /**
     * 인증하는 방식 설정
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("fairer api info")
                .description("fairer API")
                .version("1.0.0")
                .build();
    }

    private Server getServer(String profile, String url, String desc) {
        return new Server(profile, url, desc, Collections.emptyList(), Collections.emptyList());
    }


}
