package com.depromeet.fairer.dto.team.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@ApiModel(value = "팀 생성 요청 객체", description = "팀 생성 요청 객체")
public class TeamCreateRequestDto {

    @ApiModelProperty(value = "팀 이름")
    @NotBlank
    private String teamName;
}
