package com.depromeet.fairer.domain.houseworkComplete;

import com.depromeet.fairer.domain.housework.HouseWork;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "housework_complete")
@Getter @Setter
@ToString(exclude = {"houseWork"})
@Builder
@NoArgsConstructor @AllArgsConstructor
public class HouseworkComplete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housework_complete_id", columnDefinition = "BIGINT", nullable = false)
    private Long houseWorkCompleteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housework_id")
    private HouseWork houseWork;

    @Column(name = "scheduled_date", columnDefinition = "DATE", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "success_datetime", columnDefinition = "DATETIME")
    private LocalDateTime successDateTime;

    public HouseworkComplete(LocalDate scheduledDate, HouseWork houseWork, LocalDateTime successDateTime){
        this.setScheduledDate(scheduledDate);
        this.setHouseWork(houseWork);
        this.setSuccessDateTime(successDateTime);
    }
}
