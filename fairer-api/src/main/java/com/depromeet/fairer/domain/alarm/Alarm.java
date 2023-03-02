package com.depromeet.fairer.domain.alarm;

import com.depromeet.fairer.domain.base.BaseTimeEntity;
import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "alarm")
@Builder
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long alarmId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "scheduled_time_status", columnDefinition = "BIT", nullable = false)
    private Boolean scheduledTimeStatus;

    @Column(name = "not_complete_status", columnDefinition = "BIT", nullable = false)
    private Boolean notCompleteStatus;

    public static Alarm create(Member member) {
        return Alarm.builder()
                .member(member)
                .scheduledTimeStatus(true)
                .notCompleteStatus(true)
                .build();
    }
}
