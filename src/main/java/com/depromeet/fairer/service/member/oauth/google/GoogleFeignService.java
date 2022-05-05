package com.depromeet.fairer.service.member.oauth.google;

import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.dto.member.oauth.GoogleUserInfo;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GoogleFeignService {
    private final GoogleFeignClient googleFeignClient;
    private final PasswordEncoder passwordEncoder;

    private final String password = "autoPassword"; // TODO 환경변수로 변경
    private final String clientId = "973504120779-eqs2eb6680p6kmg0u4qo474tgp7nu56d.apps.googleusercontent.com";
    private final String clientSecret = "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";
    private final String redirectUri = "http://localhost:8080/login/oauth2/code/google";
    private final String grantType = "authorization_code";

    public OAuthAttributes getUserInfo(String accessToken) {
        accessToken = "Bearer " + accessToken.replace("Bearer", "").trim();
        log.info("Access Token: {}", accessToken);

        GoogleUserInfo googleUserInfo = googleFeignClient.getGoogleUserInfo(accessToken);
        log.info("email: {}", googleUserInfo.getEmail());
        log.info("name: {}", googleUserInfo.getName());

        return OAuthAttributes.builder()
                .email(StringUtils.isBlank(googleUserInfo.getEmail()) ? googleUserInfo.getId() : googleUserInfo.getEmail()) // 이메일 동의 x 경우
                .name(googleUserInfo.getName())
                .socialType(SocialType.GOOGLE)
                .password(passwordEncoder.encode(password))
                .build();
    }

    public String getAccessToken(String authorizationCode) {
        final OauthTokenResponse response = googleFeignClient.getAccessToken(authorizationCode, clientId, clientSecret, redirectUri, grantType);
        log.info("response: {}", response.toString());
        return response.getAccessToken();
    }
}
