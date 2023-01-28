package com.depromeet.fairer.dto.houseworkComplete.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "멤버별 이번달 완료 집안일 통계 요청 객체", description = "멤버별 이번달 완료 집안일 통계 요청 객체")
public class TeamHouseWorkStatisticPerMonthRequestDto {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "조회 연도")
    private int year;

    @NotNull
    @ApiModelProperty(value = "조회 월")
    @Min(0)
    @Max(13)
    private int month;
}
