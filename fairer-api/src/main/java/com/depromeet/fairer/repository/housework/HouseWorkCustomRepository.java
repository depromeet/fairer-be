package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.team.Team;
import java.time.LocalDate;
import java.util.List;

public interface HouseWorkCustomRepository {
    Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate);

    List<Object[]> getCycleHouseWorkQuery(LocalDate date, Long memberId);

    List<Object[]> getCycleHouseWorkByTeamQuery(LocalDate date, Team team);
}
