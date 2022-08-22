package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.preset.Preset;
import com.depromeet.fairer.domain.preset.Space;
import com.depromeet.fairer.dto.preset.response.HouseWorkPresetResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "presets", description = "프리셋 API")
@RequestMapping("/api/preset")
public class PresetController {

    /**
     * 공간 -> 집안일 프리셋 조회
     * @param space 공간
     * @return 집안일 이름 list
     */
    @Tag(name = "presets")
    @GetMapping(value = "{space}")
    public ResponseEntity<HouseWorkPresetResponseDto> getHouseWorkPreset(@PathVariable Space space){
        return ResponseEntity.ok(new HouseWorkPresetResponseDto(space.name(), Preset.getHouseworkNameListBySpace(space)));
    }

    @Tag(name = "presets")
    @GetMapping(value = "")
    public ResponseEntity<List<HouseWorkPresetResponseDto>> getAllPreset(){
        List<HouseWorkPresetResponseDto> response = new ArrayList<>();
        for (Space space : Space.values()) {
            if (space.equals(Space.ETC)) {
                continue;
            }
            response.add(new HouseWorkPresetResponseDto(space.name(), Preset.getHouseworkNameListBySpace(space)));
        }
        return ResponseEntity.ok(response);
    }
}
