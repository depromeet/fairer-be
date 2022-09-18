package com.depromeet.fairer.api;

import com.depromeet.fairer.dto.alarm.request.AlarmRequestDto;
import com.depromeet.fairer.dto.alarm.response.AlarmResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.alarm.AlarmService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
@Tag(name = "alarm", description = "알람 API")
public class AlarmController {
    private final AlarmService alarmService;

    @Tag(name = "alarm")
    @GetMapping("/status")
    @ApiOperation(value = "알람 설정 조회")
    public ResponseEntity<AlarmResponseDto> getAlarmStatus(@ApiIgnore @RequestMemberId Long memberId) {
        return ResponseEntity.ok(alarmService.getAlarmStatus(memberId));
    }

    @Tag(name = "alarm")
    @PutMapping("/status")
    @ApiOperation(value = "알람 설정 수정")
    public ResponseEntity<AlarmResponseDto> updateAlarmStatus(@ApiIgnore @RequestMemberId Long memberId,
                                                             @RequestBody @Valid AlarmRequestDto request) {
        return ResponseEntity.ok(alarmService.updateAlarmStatus(memberId, request));
    }
}
