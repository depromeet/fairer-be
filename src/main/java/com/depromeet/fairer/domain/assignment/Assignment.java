package com.depromeet.fairer.domain.assignment;

import com.depromeet.fairer.domain.housework.Housework;
import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "assignment")
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class Assignment {

    @Id
    @Column(name = "assignment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "housework_id")
    private Housework housework;
}
