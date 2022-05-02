package com.depromeet.fairer.repository;

import com.depromeet.fairer.domain.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
}