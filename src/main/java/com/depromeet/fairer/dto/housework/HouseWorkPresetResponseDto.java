package com.depromeet.fairer.dto.housework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HouseWorkPresetResponseDto {
    private String space;
    private List<String> HouseWorks;
}
