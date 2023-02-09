package com.depromeet.fairer.dto.houseworkComplete.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "팀 멤버 집안일 별 완료 통계 반환 객체")
public class MonthlyHouseWorkStatisticByHouseWorkResponseDto {

    @ApiModelProperty(value = "멤버 별 집안일 완료 통계 객체 리스트")
    private List<MemberHouseWorkStatisticByHouseWorkDto> houseWorkStatics;

    public static MonthlyHouseWorkStatisticByHouseWorkResponseDto of(
            List<MemberHouseWorkStatisticByHouseWorkDto> houseWorkStatics
    ) {
        return new MonthlyHouseWorkStatisticByHouseWorkResponseDto(houseWorkStatics);
    }
}
