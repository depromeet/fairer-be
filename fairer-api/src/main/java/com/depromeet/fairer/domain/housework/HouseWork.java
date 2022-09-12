package com.depromeet.fairer.domain.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.base.BaseTimeEntity;
import com.depromeet.fairer.domain.housework.constant.RepeatCycle;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.preset.Space;
import com.depromeet.fairer.domain.repeatexception.RepeatException;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.global.util.DateTimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.valueOf;

@Entity
@Table(name = "housework")
@Getter
@Setter
@ToString(exclude = {"assignments", "houseworkComplete"})
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseWork extends BaseTimeEntity {
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

    @Column(name = "scheduled_time", columnDefinition = "TIME")
    private LocalTime scheduledTime;

    @Column(name = "success_datetime", columnDefinition = "DATETIME")
    private LocalDateTime successDateTime;

    @Column(name = "success", columnDefinition = "BIT", nullable = false)
    private Boolean success;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder.Default
    @OneToMany(mappedBy = "houseWork", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_cycle", columnDefinition = "VARCHAR(30)")
    private  RepeatCycle repeatCycle;

    @Column(name = "repeat_day_of_week", columnDefinition = "VARCHAR(30)")
    private String repeatDayOfWeek;

    @Column(name = "repeat_end_date", columnDefinition = "DATE")
    private LocalDate repeatEndDate; //endDate 당일까지 반복 포함

    @OneToMany(mappedBy = "houseWork", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseworkComplete> houseworkCompleteList;

    @OneToMany(mappedBy = "houseWork", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepeatException> repeatExceptionList;

    private String rrule;

    public boolean isIncludingDate(LocalDate date) {
        if (repeatEndDate != null && date.isAfter(repeatEndDate)) {
                return false;
        }
        return repeatDayOfWeek.contains(DateTimeUtils.convertDayOfWeekToKor(date.getDayOfWeek()))
                && date.isAfter(scheduledDate);
    }

    public void updateRepeatEndDateByCycle(LocalDate deleteStandardDate) {
        if (repeatCycle == RepeatCycle.EVERY) {
            repeatEndDate = deleteStandardDate.minusDays(1);
        } else if (repeatCycle == RepeatCycle.WEEKLY) {
            repeatEndDate = deleteStandardDate.minusWeeks(1);
        } else if (repeatCycle == RepeatCycle.MONTHLY) {
            repeatEndDate = deleteStandardDate.minusMonths(1);
        }
    }
}
