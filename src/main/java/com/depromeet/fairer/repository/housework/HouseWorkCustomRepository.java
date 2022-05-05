package com.depromeet.fairer.repository.housework;

import java.time.LocalDate;

public interface HouseWorkCustomRepository {
    Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate);
}
