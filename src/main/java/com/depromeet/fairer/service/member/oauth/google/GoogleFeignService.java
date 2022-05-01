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
}
