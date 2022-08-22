package com.depromeet.fairer.service.member.oauth.google;

import com.depromeet.fairer.dto.member.oauth.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(url = "https://www.googleapis.com/oauth2", name = "GoogleInfoClient")
public interface GoogleInfoFeignClient {

    @GetMapping(value = "/v2/userinfo", produces = "application/json", consumes = "application/json")
    GoogleUserInfo getGoogleUserInfo(@RequestHeader("Authorization") String accessToken);
}
