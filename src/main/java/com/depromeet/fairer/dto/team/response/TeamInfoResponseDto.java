package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.team.Team;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class TeamInfoResponseDto {
    private Long teamId;
    private String teamName;
    private List<TeamMemberResponseDto> members;

    public static TeamInfoResponseDto from(Team team) {
        return TeamInfoResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .members(TeamMemberResponseDto.toList(team.getMembers()))
                .build();
    }
}
