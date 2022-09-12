package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.team.Team;
import java.time.LocalDate;
import java.util.List;

public interface HouseWorkCustomRepository {
    Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate);

    List<HouseWork> getCycleHouseWork(LocalDate fromDate, LocalDate toDate, Long memberId);

    List<HouseWork> getCycleHouseWorkByTeam(LocalDate from, LocalDate to, Team team);
}
