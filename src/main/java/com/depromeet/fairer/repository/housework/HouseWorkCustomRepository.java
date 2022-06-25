package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.HouseWork;

import java.time.LocalDate;
import java.util.List;

public interface HouseWorkCustomRepository {
    Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate);

    List<HouseWork> getHouseWorkAndAssignee(Long memberId, LocalDate localDate);


    // List<HouseWork> getHouseWorkList(LocalDate scheduledDate, Long memberId);
}
