package com.depromeet.fairer.dto.housework;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class HouseWorkStatusRequestDto {
    private boolean toBeStatus;

    public HouseWorkStatusRequestDto(boolean toBeStatus){
        this.toBeStatus = toBeStatus;
    }
}
