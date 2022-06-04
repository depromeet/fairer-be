package com.depromeet.fairer.service.member.oauth.google;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(url = "https://oauth2.googleapis.com", name = "GoogleAuthClient")
public interface GoogleAuthFeignClient {

    @PostMapping(value = "/token", produces = "application/json", consumes = "application/json",
    headers = {"Content-Length: 0", "Content-type: application/x-www-form-urlencoded"})
    String getAccessToken(@RequestParam(value = "code") String code,
                                      @RequestParam(value = "client_id") String clientId,
                                      @RequestParam(value = "client_secret") String clientSecret,
                                      @RequestParam(value = "redirect_uri") String redirectUri,
                                      @RequestParam(value = "grant_type") String grantType);
}
