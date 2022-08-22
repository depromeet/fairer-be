package com.depromeet.fairer.dto.preset.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class HouseWorkPresetResponseDto {
    private String space;
    private List<String> houseWorks;
}
