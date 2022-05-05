package com.depromeet.fairer.service.member.oauth.google;

import com.depromeet.fairer.dto.member.oauth.GoogleUserInfo;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(url = "https://www.googleapis.com/oauth2", name = "GoogleClient")
public interface GoogleFeignClient {

    @GetMapping(value = "/v2/userinfo", produces = "application/json", consumes = "application/json")
    GoogleUserInfo getGoogleUserInfo(@RequestHeader("Authorization") String accessToken);

    @PostMapping(value = "/v4/token")
    @Headers("Content-type: application/x-www-form-urlencoded;charset=utf-8")
    OauthTokenResponse getAccessToken(@RequestParam(value = "code") String code,
                          @RequestParam(value = "client_id") String clientId,
                          @RequestParam(value = "client_secret") String clientSecret,
                          @RequestParam(value = "redirect_uri") String redirectUri,
                          @RequestParam(value = "grant_type") String grantType);
}
