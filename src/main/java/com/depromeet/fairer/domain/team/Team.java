package com.depromeet.fairer.domain.team;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long teamId;

    @NotNull
    @Column(name = "team_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team")
    private Set<Member> members;

    @OneToMany(mappedBy = "team")
    private List<HouseWork> houseWorks;

    @Builder
    public Team(Member member, String teamName) {
        this.teamName = teamName;

        this.houseWorks = new ArrayList<>();
        if (this.members == null) {
            members = new HashSet<>();
        }

        member.joinTeam(this);
    }

}
