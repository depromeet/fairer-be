package com.depromeet.fairer.domain.houseworkComplete;

import com.depromeet.fairer.domain.feedback.Feedback;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "houseworkComplete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbackList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public int countFeedback() {
        return this.feedbackList.size();
    }

    public HouseworkComplete(LocalDate scheduledDate, HouseWork houseWork, LocalDateTime successDateTime, Member member) {
        this.setScheduledDate(scheduledDate);
        this.setHouseWork(houseWork);
        this.setSuccessDateTime(successDateTime);
        this.setMember(member);
    }
}