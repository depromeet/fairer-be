package com.depromeet.fairer.repository.repeatexception;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RepeatExceptionCustomRepository {

    void deleteAfterStandardDate(Long houseWorkId, LocalDate deleteStandardDate);
}
