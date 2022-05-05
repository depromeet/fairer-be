package com.depromeet.fairer.domain.team;

import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long teamId;

    @OneToMany(mappedBy = "team")
    private List<Member> members;
}
