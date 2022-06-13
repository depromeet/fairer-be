package com.depromeet.fairer.domain.rule;

import com.depromeet.fairer.domain.team.Team;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rule")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long ruleId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "rule_name")
    private String ruleName;

}
