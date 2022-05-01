package com.depromeet.fairer.service.member.oauth.google;

import com.depromeet.fairer.dto.member.oauth.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(url = "https://www.googleapis.com/oauth2/v2/userinfo", name = "GoogleClient")
public interface GoogleFeignClient {

    @GetMapping(value = "/", produces = "application/json", consumes = "application/json")
    GoogleUserInfo getGoogleUserInfo(@RequestHeader("Authorization") String accessToken);
}
