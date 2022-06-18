package com.depromeet.fairer.dto.team.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@ApiModel(value = "팀 초대 코드 보기 반환 객체", description = "팀 초대 코드 보기 반환 객체")
public class TeamInviteCodeResponseDto {

    @ApiModelProperty(value = "팀 초대 코드")
    private String inviteCode;

    public static TeamInviteCodeResponseDto from(String inviteCode) {
        return new TeamInviteCodeResponseDto(inviteCode);
    }
}
