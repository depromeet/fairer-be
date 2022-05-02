package com.depromeet.fairer.dto.housework;

import lombok.Data;

@Data
public class HouseWorkStatusResponseDto {
    private Long houseWorkId;
    private Boolean success;

    public HouseWorkStatusResponseDto(Long houseWorkId, boolean success){
        this.houseWorkId = houseWorkId;
        this.success = success;
    }
}
