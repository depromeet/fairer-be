package com.depromeet.fairer.repository;

import com.depromeet.fairer.dto.member.MemberDto;

import java.util.List;
import java.time.LocalDate;

public interface HouseWorkCustomRepository {
    // "houseworks" 배열에 members 추가
    List<MemberDto> addMemberDtoById(Long houseWorkId);
    Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate);
}
