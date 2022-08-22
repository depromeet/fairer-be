package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.domain.member.Member;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> getMemberDtoListByHouseWorkId(Long houseWorkId);
}
