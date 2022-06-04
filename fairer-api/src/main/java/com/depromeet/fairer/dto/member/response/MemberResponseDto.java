package com.depromeet.fairer.dto.member.response;


import com.depromeet.fairer.domain.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String profilePath;
    private String memberName;
    private String statusMessage;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .profilePath(member.getProfilePath())
                .memberName(member.getMemberName())
                .statusMessage(member.getStatusMessage())
                .build();
    }
}
