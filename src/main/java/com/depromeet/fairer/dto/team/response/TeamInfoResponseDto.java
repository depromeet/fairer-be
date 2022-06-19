package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.member.MemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@ApiModel(value = "팀 정보 반환 객체", description = "팀 정보 반환 객체")
@Data
@Builder(toBuilder = true)
public class TeamInfoResponseDto {
    @ApiModelProperty(value = "팀 ID", example = "1")
    private Long teamId;

    @ApiModelProperty(value = "팀 이름", example = "우리집")
    private String teamName;

    @ApiModelProperty(value = "팀 멤버")
    private List<MemberDto> members;

    public static TeamInfoResponseDto from(Team team) {
        return TeamInfoResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .members(MemberDto.toList(team.getMembers()))
                .build();
    }
}
