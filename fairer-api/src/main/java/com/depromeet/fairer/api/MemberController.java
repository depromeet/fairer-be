package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.member.constant.ProfileImage;
import com.depromeet.fairer.dto.common.CommonApiResult;
import com.depromeet.fairer.dto.member.request.MemberUpdateRequestDto;
import com.depromeet.fairer.dto.member.response.MemberProfileImageResponseDto;
import com.depromeet.fairer.dto.member.response.MemberResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "members", description = "멤버 API")
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Value("${profile.domain}")
    private String profileImageDomain;
    @Value("${profile.default-path}")
    private String profileImageDefaultPath;

    @Tag(name = "members")
    @GetMapping("/me")
    @ApiOperation(value = "내 정보 조회")
    public ResponseEntity<MemberResponseDto> getMe(@ApiIgnore @RequestMemberId Long memberId) {
        return ResponseEntity.ok(MemberResponseDto.from(memberService.find(memberId)));
    }

    /***
     * 기본적으로 제공해주는 프로필 이미지는 profileImageDefaultPath 하위에 보관
     * 추후에 유저가 업로드 하는 프로필 이미지는 각 유저 폴더에 보관
     */
    @Tag(name = "members")
    @GetMapping("/profile-image")
    @ApiOperation(value = "기본 프로필 이미지 리스트 조회")
    public ResponseEntity<MemberProfileImageResponseDto> getDefaultProfileImageList() {
        return ResponseEntity.ok(
                MemberProfileImageResponseDto.builder()
                        .bigImageList(ProfileImage.getBigImageFullPathList(profileImageDomain, profileImageDefaultPath))
                        .build());
    }

    @Tag(name = "members")
    @ApiOperation(value = "멤버 업데이트", notes = "멤버 정보 업데이트<br/><br/>" +
            "멤버 이름<br/>프로필 url")
    @PatchMapping(value = "")
    public ResponseEntity<CommonApiResult> updateTeam(@ApiIgnore @RequestMemberId Long memberId, @Valid @RequestBody MemberUpdateRequestDto requestDto) {
        // TODO 이름, url 정규식 검증
        memberService.updateMember(memberId, requestDto.getMemberName(), requestDto.getProfilePath(), requestDto.getStatusMessage());
        return ResponseEntity.ok(CommonApiResult.createOk("멤버 정보가 업데이트 되었습니다."));
    }
}
