package com.depromeet.fairer.repository.houseworkcomplete;

import com.querydsl.core.Tuple;

import java.util.List;

public interface HouseWorkCompleteCustomRepository {
    List<Tuple> getTeamHouseWorkStatisticPerMonthByTeamIdAndHouseWorkName(Long teamId, int year, int month, String houseWorkName);
}
