package com.depromeet.fairer.api.fcm;

import com.depromeet.fairer.dto.fcm.request.FCMMessageRequest;
import com.depromeet.fairer.dto.fcm.request.HurryMessageRequest;
import com.depromeet.fairer.dto.fcm.request.SaveTokenRequest;
import com.depromeet.fairer.dto.fcm.response.FCMMessageResponse;
import com.depromeet.fairer.dto.fcm.response.SaveTokenResponse;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.fcm.FCMService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "fcm", description = "FCM 관련 API")
@RequestMapping(value = "/api/fcm")
public class FCMController {
    private final FCMService fcmService;

    @Tag(name = "fcm")
    @PostMapping("/token")
    public ResponseEntity<SaveTokenResponse> saveToken(@Valid @RequestBody SaveTokenRequest request, @ApiIgnore @RequestMemberId Long memberId) {
        return ResponseEntity.ok(fcmService.saveToken(request, memberId));
    }

    @Tag(name = "fcm")
    @PostMapping("/message")
    public ResponseEntity<FCMMessageResponse> sendMessage(@Valid @RequestBody FCMMessageRequest request) {
        return ResponseEntity.ok(fcmService.sendMessage(request));
    }

    @Tag(name = "fcm")
    @PostMapping("/hurry")
    @ApiOperation(value = "재촉하기 api", notes = "재촉할 housework id -> 할당된 멤버 모두에게 ")
    public ResponseEntity<List<FCMMessageResponse>> sendHurry(@Valid @RequestBody HurryMessageRequest request) {
        return ResponseEntity.ok(fcmService.sendHurry(request.getHouseworkId(), request.getScheduledDate()));
    }

    @Tag(name = "fcm")
    @PostMapping("/update/{memberId}")
    @ApiOperation(value = "업데이트 알림 api")
    public ResponseEntity<FCMMessageResponse> sendUpdate(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(fcmService.sendUpdate(memberId));
    }

}
