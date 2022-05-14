package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> findByEmail(String email);

    Member findAllByMemberId(Long memberId);
}