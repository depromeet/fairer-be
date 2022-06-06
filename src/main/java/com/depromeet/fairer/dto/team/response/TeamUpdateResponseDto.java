package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.team.Team;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@ApiModel(value = "팀 업데이트 반환 객체", description = "팀 업데이트 반환 객체")
public class TeamUpdateResponseDto {

    @ApiModelProperty(value = "팀 ID")
    private Long teamId;

    @ApiModelProperty(value = "팀 이름")
    private String teamName;


    public static TeamUpdateResponseDto from(Team team) {
        return TeamUpdateResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .build();
    }
}
