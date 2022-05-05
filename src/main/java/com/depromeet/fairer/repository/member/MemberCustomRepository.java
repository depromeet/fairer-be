package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.dto.member.MemberDto;

import java.util.List;

public interface MemberCustomRepository {
    List<MemberDto> getMemberDtoListByHouseWorkId(Long houseWorkId);
}
