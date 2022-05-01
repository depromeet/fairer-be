package com.depromeet.fairer.repository;

import com.depromeet.fairer.dto.member.MemberDto;

import java.util.List;

public interface HouseWorkCustomRepository {
    // "houseworks" 배열에 members 추가
    List<MemberDto> addMemberDtoById(Long houseWorkId);

}
