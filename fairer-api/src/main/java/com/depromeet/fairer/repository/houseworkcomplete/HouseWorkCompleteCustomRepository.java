package com.depromeet.fairer.repository.houseworkcomplete;

import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface HouseWorkCompleteCustomRepository {
    List<Tuple> findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkName(Long teamId, YearMonth month, String houseWorkName);
}
