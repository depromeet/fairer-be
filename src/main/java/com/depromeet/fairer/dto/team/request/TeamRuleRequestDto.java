package com.depromeet.fairer.dto.team.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "팀 규칙 요청 객체", description = "팀 규칙 요청 객체")
public class TeamRuleRequestDto {

    @ApiModelProperty(value = "규칙 이름")
    private String ruleName;
}
