package com.depromeet.fairer.dto.preset.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HouseWorkPresetListResponseDto {
    private List<HouseWorkPresetResponseDto> preset;
}
