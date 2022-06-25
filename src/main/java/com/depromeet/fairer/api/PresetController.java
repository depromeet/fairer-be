package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.preset.constant.Space;
import com.depromeet.fairer.dto.preset.response.HouseWorkPresetListResponseDto;
import com.depromeet.fairer.dto.preset.response.HouseWorkPresetResponseDto;
import com.depromeet.fairer.service.PresetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "presets", description = "프리셋 API")
@RequestMapping("/api/preset")
public class PresetController {
    private final PresetService presetService;

    /**
     * 공간 -> 집안일 프리셋 조회
     * @param space 공간
     * @return 집안일 이름 list
     */
    @Tag(name = "presets")
    @GetMapping(value = "{space}")
    public ResponseEntity<HouseWorkPresetResponseDto> getHouseWorkPreset(@PathVariable Space space){
        return ResponseEntity.ok(presetService.getHouseWorkPreset(space));
    }

    @Tag(name = "presets")
    @GetMapping(value = "")
    public ResponseEntity<HouseWorkPresetListResponseDto> getPreset(){
        return ResponseEntity.ok(presetService.getPreset());
    }
}
