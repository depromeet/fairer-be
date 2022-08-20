package com.depromeet.fairer.dto.team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@ApiModel(value = "팀 나가기 요청 객체", description = "팀 나가기 요청 객체")
public class TeamLeaveRequestDto {

    @ApiModelProperty(value = "나갈 팀 ID")
    private Long teamId;
}
