package com.depromeet.fairer.repository.rule;

import com.depromeet.fairer.domain.rule.Rule;
import com.depromeet.fairer.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findAllByTeam(Team team);
    long countByTeam(Team team);
}
