package com.depromeet.fairer.dto.housework.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class HouseWorksCreateResponseDto {
    @ApiModelProperty(value = "생성된 집안일 목록")
    private List<HouseWorkResponseDto> houseWorks;

    public HouseWorksCreateResponseDto(List<HouseWorkResponseDto> houseWorks) {
        this.houseWorks = houseWorks;
    }
}
