package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.member.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class TeamMemberListResponseDto {
    private List<TeamMemberResponseDto> members;

    public static TeamMemberListResponseDto from(Set<Member> members) {
        List<TeamMemberResponseDto> membersDto = members.stream().map(TeamMemberResponseDto::from).collect(Collectors.toList());
        return TeamMemberListResponseDto.builder()
                .members(membersDto)
                .build();
    }
}
