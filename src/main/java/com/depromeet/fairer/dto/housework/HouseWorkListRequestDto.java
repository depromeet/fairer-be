package com.depromeet.fairer.dto.housework;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class HouseWorkListRequestDto {
    @NotNull
    private List<HouseWorkRequestDto> houseWorks;
}
