package com.depromeet.fairer.dto.team.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@ApiModel(value = "팀 업데이트 요청 객체", description = "팀 업데이트 요청 객체")
public class TeamUpdateRequestDto {

    @ApiModelProperty(value = "팀 이름")
    private String teamName;


}
