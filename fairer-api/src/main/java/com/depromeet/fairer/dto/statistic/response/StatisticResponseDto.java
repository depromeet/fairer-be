package com.depromeet.fairer.dto.statistic.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(value = "통계 리스트 조회 반환 객체", description = "통계 리스트 조회 반환 객체")
public class StatisticResponseDto {

    @ApiModelProperty(value = "집안일 이름")
    private String houseWorkName;

    @ApiModelProperty(value = "완료 개수")
    private Integer houseWorkCount;

    public static StatisticResponseDto from(String houseWorkName, Integer houseWorkCount){

        return new StatisticResponseDtoBuilder()
                .houseWorkName(houseWorkName)
                .houseWorkCount(houseWorkCount)
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @ApiModel(value = "통계 리스트 조회 최종 반환 객체", description = "통계 리스트 조회 최종 반환 객체")
    public static class StatisticResponseDtoList {

        @ApiModelProperty(value = "통계 리스트")
        private List<StatisticResponseDto> statisticResponseDtos;

    }

}
