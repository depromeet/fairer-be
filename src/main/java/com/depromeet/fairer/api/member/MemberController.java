package com.depromeet.fairer.api.member;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.common.CommonApiResult;
import com.depromeet.fairer.dto.member.MemberUpdateRequestDto;
import com.depromeet.fairer.dto.team.request.TeamUpdateRequestDto;
import com.depromeet.fairer.dto.team.response.TeamUpdateResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "멤버 업데이트", notes = "멤버 정보 업데이트<br/><br/>" +
            "멤버 이름<br/>프로필 url")
    @PatchMapping(value = "")
    public ResponseEntity<CommonApiResult> updateTeam(@ApiIgnore @RequestMemberId Long memberId, @RequestBody MemberUpdateRequestDto requestDto) {
        // TODO 이름, url 정규식 검증
        memberService.updateMember(memberId, requestDto.getMemberName(), requestDto.getProfileUrl());
        return ResponseEntity.ok(CommonApiResult.createOk("멤버 정보가 업데이트 되었습니다."));
    }
}
