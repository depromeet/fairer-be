package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.member.Member;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TeamMemberResponseDto {
    private Long teamId;
    private Long memberId;
    private String memberName;

    public static TeamMemberResponseDto from(Member member) {
        return TeamMemberResponseDto.builder()
                .teamId(member.getTeam().getTeamId())
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .build();
    }
}
