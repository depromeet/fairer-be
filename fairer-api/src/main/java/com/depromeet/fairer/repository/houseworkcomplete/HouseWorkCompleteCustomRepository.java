package com.depromeet.fairer.repository.houseworkcomplete;

import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.vo.houseWorkComplete.HouseWorkCompleteStatisticsVo;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface HouseWorkCompleteCustomRepository {
    List<HouseWorkCompleteStatisticsVo> findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkName(Long teamId, YearMonth month, String houseWorkName);

    List<HouseWorkCompleteStatisticsVo> findMonthlyHouseWorkRanking(Long teamId, YearMonth month);

    Long findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkNameV2(Member member, YearMonth from, String houseWorkName);

    Long getMonthlyCountByMember(Member member, YearMonth month);
}
