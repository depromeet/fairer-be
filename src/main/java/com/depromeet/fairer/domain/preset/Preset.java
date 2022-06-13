package com.depromeet.fairer.domain.preset;

import com.depromeet.fairer.domain.base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "preset")
@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Preset extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preset_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long presetId;

    @Column(name = "space_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String presetSpaceName;

    @Column(name = "housework_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String presetHouseWorkName;
}
