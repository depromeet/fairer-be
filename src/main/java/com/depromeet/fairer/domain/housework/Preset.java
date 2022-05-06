package com.depromeet.fairer.domain.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "preset")
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class Preset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preset_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long presetId;

    @Column(name = "space_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String presetSpaceName;

    @Column(name = "housework_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String presetHouseWorkName;
}
