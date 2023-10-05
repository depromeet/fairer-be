package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    List<Member> findAllByTeam(Team team);

    @EntityGraph(attributePaths = {"team"})
    Optional<Member> findWithTeamByEmail(String email);

    @EntityGraph(attributePaths = {"team"})
    Optional<Member> findWithTeamByMemberId(Long memberId);

    Optional<Member> findByEmail(String email);

}