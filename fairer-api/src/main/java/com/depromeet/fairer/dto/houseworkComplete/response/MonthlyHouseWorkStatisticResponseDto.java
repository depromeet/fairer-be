package com.depromeet.fairer.dto.houseworkComplete.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "팀 멤버별 집안일 완료 통계 반환 객체")
public class MonthlyHouseWorkStatisticResponseDto {

    @ApiModelProperty(value = "멤버 별 집안일 완료 통계 객체 리스트")
    private List<MemberHouseWorkStatisticDto> houseWorkStatics;

    public static MonthlyHouseWorkStatisticResponseDto of(List<MemberHouseWorkStatisticDto> houseWorkStatics) {
        return new MonthlyHouseWorkStatisticResponseDto(houseWorkStatics);
    }
}