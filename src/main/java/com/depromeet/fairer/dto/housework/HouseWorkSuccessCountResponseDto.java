package com.depromeet.fairer.dto.housework;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class HouseWorkSuccessCountResponseDto {
    private Long count;

    public static HouseWorkSuccessCountResponseDto of(Long count) {
        return HouseWorkSuccessCountResponseDto.builder().count(count).build();
    }
}
