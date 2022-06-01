package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@ApiModel(value = "Team 반환 객체", description = "Team 반환 객체")
@NoArgsConstructor @AllArgsConstructor
public class TeamResponseDto {

    @ApiModelProperty(value = "팀 ID")
    private Long teamId;

    @ApiModelProperty(value = "팀에 소속된 멤버 이름 리스트")
    private List<String> memberNames; // 정책에 따라 이메일 등 다른 항목으로 변경 가능

    public static TeamResponseDto from(Team team) {

        return new TeamResponseDtoBuilder()
                .teamId(team.getTeamId())
                .memberNames(team.getMembers().stream()
                        .map(Member::getMemberName)
                        .collect(Collectors.toList()))
                .build();
    }
}
