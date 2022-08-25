package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.team.Team;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@ApiModel(value = "Team 생성 반환 객체", description = "Team 생성 반환 객체")
@NoArgsConstructor @AllArgsConstructor
public class TeamCreateResponseDto {

    @ApiModelProperty(value = "팀 ID")
    private Long teamId;

    @ApiModelProperty(value = "초대 코드")
    private String inviteCode;



    public static TeamCreateResponseDto from(Team team) {

        return new TeamCreateResponseDtoBuilder()
                .teamId(team.getTeamId())
                .inviteCode(team.getInviteCode())
                .build();
    }
}
