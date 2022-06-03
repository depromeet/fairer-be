package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@ApiModel(value = "팀 참여 반환 객체", description = "팀 참여 반환 객체")
public class TeamJoinResponseDto {

    @ApiModelProperty(value = "팀 ID")
    private Long teamId;

    @ApiModelProperty(value = "그룹 참여자 이름 리스트")
    private List<String> memberNames;

    public static TeamJoinResponseDto from(Team team) {
        return TeamJoinResponseDto.builder()
                .teamId(team.getTeamId())
                .memberNames(team.getMembers().stream().map(Member::getMemberName).collect(Collectors.toList()))
                .build()
                ;
    }

}
