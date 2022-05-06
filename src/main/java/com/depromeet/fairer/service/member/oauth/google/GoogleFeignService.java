package com.depromeet.fairer.service.member.oauth.google;

import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.dto.member.oauth.GoogleUserInfo;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GoogleFeignService {
    private final GoogleInfoFeignClient googleInfoFeignClient;
    private final GoogleAuthFeignClient googleAuthFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${oauth2.redirectUri}")
    private String REDIRECT_URI;

    @Value("${oauth2.clientId}")
    private final String CLIENT_ID;

    @Value("${oauth2.clientSecret}")
    private final String CLIENT_SECRET;

    private final String PASSWORD = "autoPassword";
    private final String GRANT_TYPE = "authorization_code";
    private final String REQUEST_URI = "https://www.googleapis.com/oauth2/v4/token";


    public OAuthAttributes getUserInfo(String accessToken) {
        accessToken = "Bearer " + accessToken.replace("Bearer", "").trim();
        log.info("Access Token: {}", accessToken);

        GoogleUserInfo googleUserInfo = googleInfoFeignClient.getGoogleUserInfo(accessToken);
        log.info("email: {}", googleUserInfo.getEmail());
        log.info("name: {}", googleUserInfo.getName());

        return OAuthAttributes.builder()
                .email(StringUtils.isBlank(googleUserInfo.getEmail()) ? googleUserInfo.getId() : googleUserInfo.getEmail()) // 이메일 동의 x 경우
                .name(googleUserInfo.getName())
                .socialType(SocialType.GOOGLE)
                .password(passwordEncoder.encode(PASSWORD))
                .build();
    }

    public String getAccess(String authorizationCode) {
//        final OauthTokenResponse response = googleAuthFeignClient.getAccessToken(authorizationCode, clientId, clientSecret, redirectUri, grantType);
        String accessToken = getAccessToken(authorizationCode);
        log.info("access token: {}", accessToken);
//        return response.getAccessToken();
        return accessToken;
    }

    private String getAccessToken(String code) {
        final HttpEntity request = createRequest(code);
        ResponseEntity<Map> response = postRequest(request);
        log.info("plz response: {}", response.toString());
        return (String) Objects.requireNonNull(response.getBody()).get("access_token");
    }

    private HttpEntity<MultiValueMap<String, String>> createRequest(String code) {
        //HTTPHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // body 데이터를 담을 value
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("grant_type", GRANT_TYPE);
        params.add("redirect_uri", REDIRECT_URI);

        //HTTPHeader 와 HTTPBody 를 하나의 오브젝트에 담기
        return new HttpEntity<>(params, headers);
    }

    private ResponseEntity<Map> postRequest(HttpEntity request) {
        return new RestTemplate().postForEntity(REQUEST_URI, request, Map.class);
    }
}
