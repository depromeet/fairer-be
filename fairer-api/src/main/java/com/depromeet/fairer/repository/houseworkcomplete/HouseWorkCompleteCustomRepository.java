package com.depromeet.fairer.repository.houseworkcomplete;

import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.vo.houseWorkComplete.HouseWorkCompleteStatisticsVo;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface HouseWorkCompleteCustomRepository {
    List<HouseWorkCompleteStatisticsVo> findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkName(Long teamId, YearMonth month, String houseWorkName);

    List<HouseWorkCompleteStatisticsVo> findMonthlyHouseWorkRanking(Long teamId, YearMonth month);

    List<HouseworkComplete> findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkNameV2(Long memberId, YearMonth from, String houseWorkName);
}
