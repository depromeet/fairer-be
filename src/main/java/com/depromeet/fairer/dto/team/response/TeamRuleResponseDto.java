package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.team.Team;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(value = "팀 규칙 반환 객체", description = "팀 규칙 반환 객체")
public class TeamRuleResponseDto {

    @ApiModelProperty(value = "팀 id")
    private Long teamId;

    @ApiModelProperty(value = "규칙 이름 list")
    private List<String> rules;

    public static TeamRuleResponseDto from(Team team){
        return new TeamRuleResponseDtoBuilder()
                .teamId(team.getTeamId())
                .rules(team.getRules())
                .build();
    }
}
