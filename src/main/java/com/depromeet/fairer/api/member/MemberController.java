package com.depromeet.fairer.api.member;

import com.depromeet.fairer.dto.common.CommonApiResult;
import com.depromeet.fairer.dto.member.MemberUpdateRequestDto;
import com.depromeet.fairer.dto.member.request.MemberRequestDto;
import com.depromeet.fairer.dto.member.response.MemberResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMe(@RequestMemberId Long memberId) {
        return ResponseEntity.ok(MemberResponseDto.from(memberService.find(memberId)));
    }

    @PostMapping("/me")
    public ResponseEntity<MemberResponseDto> updateMe(@Valid MemberRequestDto request, @RequestMemberId Long memberId) {
        return ResponseEntity.ok(MemberResponseDto.from(memberService.updateMember(memberId, request.getMemberName(), request.getProfilePath(), request.getStatusMessage())));
    }

    @ApiOperation(value = "멤버 업데이트", notes = "멤버 정보 업데이트<br/><br/>" +
            "멤버 이름<br/>프로필 url")
    @PatchMapping(value = "")
    public ResponseEntity<CommonApiResult> updateTeam(@ApiIgnore @RequestMemberId Long memberId, @RequestBody MemberUpdateRequestDto requestDto) {
        // TODO 이름, url 정규식 검증
        memberService.updateMember(memberId, requestDto.getMemberName(), requestDto.getProfileUrl());
        return ResponseEntity.ok(CommonApiResult.createOk("멤버 정보가 업데이트 되었습니다."));
    }
}
