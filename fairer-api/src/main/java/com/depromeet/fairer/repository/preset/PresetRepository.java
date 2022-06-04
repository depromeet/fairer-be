package com.depromeet.fairer.repository.preset;

import com.depromeet.fairer.domain.preset.Preset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresetRepository extends JpaRepository<Preset, Long> {
    List<Preset> findByPresetSpaceName(String presetSpaceName);
}
