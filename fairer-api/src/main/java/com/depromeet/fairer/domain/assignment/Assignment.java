package com.depromeet.fairer.domain.assignment;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@Builder
@ToString(exclude = {"member","houseWork"})
@AllArgsConstructor @NoArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "housework_id")
    private HouseWork houseWork;
}
