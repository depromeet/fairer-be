package com.depromeet.fairer.dto.team.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel(value = "팀 참여 요청 객체", description = "팀 참여 요청 객체")
@AllArgsConstructor
@NoArgsConstructor
public class TeamJoinRequestDto {
    @NotNull
    @ApiModelProperty(value = "초대 코드")
    private String inviteCode;
}
