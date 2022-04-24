package com.depromeet.fairer.dto.housework;

import com.depromeet.fairer.api.HouseWorkController;
import lombok.Data;

import java.util.List;

@Data
public class HouseWorkListResponseDto {
    private List<HouseWorkResponseDto> houseWorks;

    public HouseWorkListResponseDto(List<HouseWorkResponseDto> houseWorks) {
        this.houseWorks = houseWorks;
    }
}
