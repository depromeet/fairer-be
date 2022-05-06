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
@Table(name = "housework")
@Getter @Setter
@ToString(exclude = {"assignments"})
@Builder
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class HouseWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housework_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long houseWorkId;

    @Enumerated(EnumType.STRING)
    @Column(name = "space_name", columnDefinition = "VARCHAR(30)", nullable = false)
    private Space space;

    @Column(name = "housework_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String houseWorkName;

    @Column(name = "scheduled_date", columnDefinition = "DATE", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "scheduled_time", columnDefinition = "TIME", nullable = false)
    private LocalTime scheduledTime;

    @Column(name = "success_datetime", columnDefinition = "DATETIME")
    private LocalDateTime successDateTime;

    @Column(name = "success", columnDefinition = "BIT", nullable = false)
    private Boolean success;

    @Builder.Default
    @OneToMany(mappedBy = "housework")
    private List<Assignment> assignments = new ArrayList<>();
}
