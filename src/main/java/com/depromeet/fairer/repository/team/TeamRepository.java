package com.depromeet.fairer.repository.team;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
}
