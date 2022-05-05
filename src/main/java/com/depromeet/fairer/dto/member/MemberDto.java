package com.depromeet.fairer.dto.member;

import com.depromeet.fairer.domain.member.Member;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long memberId;
    private String memberName;
    private String profilePath;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .profilePath(member.getProfilePath())
                .build();
    }
}

