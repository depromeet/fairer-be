package com.depromeet.fairer.repository.repeatexception;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepeatExceptionCustomRepository {

    void deleteAfterEndDate(Long houseWorkId);
}
