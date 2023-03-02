package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.housework.response.HouseWorkQueryResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface HouseWorkCustomRepository {
    Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate);

    List<HouseWorkQueryResponseDto> getCycleHouseWorkQuery(LocalDate date, Long memberId);

    List<HouseWorkQueryResponseDto> getCycleHouseWorkByTeamQuery(LocalDate date, Team team);

    List<HouseWork> getCycleHouseWorkByTeamMonth(LocalDate fromDate, LocalDate toDate, Team team);
}
