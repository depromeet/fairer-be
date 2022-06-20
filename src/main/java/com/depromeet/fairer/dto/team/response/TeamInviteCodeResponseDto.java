package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.team.constant.TeamConstant;
import com.depromeet.fairer.vo.team.InviteCodeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@ApiModel(value = "팀 초대 코드 보기 반환 객체", description = "팀 초대 코드 보기 반환 객체")
public class TeamInviteCodeResponseDto {

    @ApiModelProperty(value = "팀 초대 코드")
    private String inviteCode;

    @ApiModelProperty(value = "초대 코드 만료 시각")
    private LocalDateTime inviteCodeExpirationDateTime;

    @ApiModelProperty(value = "팀 이름")
    private String teamName;

    public static TeamInviteCodeResponseDto from(InviteCodeVo inviteCodeVo) {
        return new TeamInviteCodeResponseDto(inviteCodeVo.getInviteCode(), inviteCodeVo.getInviteCodeExpirationDateTime(), inviteCodeVo.getTeamName());
    }
}
