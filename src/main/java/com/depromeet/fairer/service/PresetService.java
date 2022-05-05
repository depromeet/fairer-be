package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.preset.Preset;
import com.depromeet.fairer.domain.preset.constant.Space;
import com.depromeet.fairer.dto.preset.response.HouseWorkPresetResponseDto;
import com.depromeet.fairer.repository.preset.PresetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresetService {
    private final PresetRepository presetRepository;

    /**
     * 공간 -> 집안일 프리셋 조회
     * @param space 공간
     * @return 집안일 이름 list
     */
    @Transactional
    public HouseWorkPresetResponseDto getHouseWorkPreset(Space space){
        List<String> houseWorks = presetRepository.findByPresetSpaceName(space.name())
                .stream().map(Preset::getPresetHouseWorkName)
                .collect(Collectors.toList());
        return new HouseWorkPresetResponseDto(space.name(), houseWorks);
    }
}
