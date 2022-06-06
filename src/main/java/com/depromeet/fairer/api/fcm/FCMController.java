package com.depromeet.fairer.api.fcm;

import com.depromeet.fairer.dto.fcm.request.FCMMessageRequest;
import com.depromeet.fairer.dto.fcm.request.SaveTokenRequest;
import com.depromeet.fairer.dto.fcm.response.FCMMessageResponse;
import com.depromeet.fairer.dto.fcm.response.SaveTokenResponse;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.fcm.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@RestController(value = "/api/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;

    @PostMapping("/token")
    public ResponseEntity<SaveTokenResponse> saveToken(@Valid SaveTokenRequest request, @ApiIgnore @RequestMemberId Long memberId) {
        return ResponseEntity.ok(fcmService.saveToken(request, memberId));
    }

    @PostMapping("/message")
    public ResponseEntity<FCMMessageResponse> sendMessage(@Valid FCMMessageRequest request) {
        return ResponseEntity.ok(fcmService.sendMessage(request));
    }
}
