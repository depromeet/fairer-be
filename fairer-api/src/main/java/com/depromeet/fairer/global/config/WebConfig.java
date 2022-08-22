package com.depromeet.fairer.global.config;

import com.depromeet.fairer.global.config.interceptor.AuthInterceptor;
import com.depromeet.fairer.repository.memberToken.MemberTokenRepository;
import com.depromeet.fairer.service.member.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final MemberTokenRepository memberTokenRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/oauth/login", "/api/member/profile-image", "/api/fcm/message");
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(tokenProvider, memberTokenRepository);
    }

    /**
     * 다른 출처의 자원을 공유할 수 있도록 설정하는 권한 체제
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // CORS 를 적용할 URL 패턴
                .allowedOrigins("*") // 자원 공유를 허락할 Origin 지정; 모든 Origin 허락
                .allowedMethods( // 허용할 HTTP method 지정
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name()
                );
    }
}
