package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface MemberCustomRepository {
    List<Member> getMemberDtoListByHouseWorkId(Long houseWorkId);

    List<Member> getMyTeamMembers(Long memberId);
}
