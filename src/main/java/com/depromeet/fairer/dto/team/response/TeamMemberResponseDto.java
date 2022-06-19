package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.member.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static List<TeamMemberResponseDto> toList(Set<Member> members) {
        return members.stream().map(TeamMemberResponseDto::from).collect(Collectors.toList());
    }
}
