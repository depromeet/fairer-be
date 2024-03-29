package com.depromeet.fairer.repository.repeatexception;

import com.depromeet.fairer.domain.repeatexception.RepeatException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatExceptionRepository extends JpaRepository<RepeatException, Long>, RepeatExceptionCustomRepository {

    @Modifying(clearAutomatically = true)
    @Query("delete from RepeatException R where R.houseWork.houseWorkId = :houseWorkId and R.exceptionDate > R.houseWork.repeatEndDate")
    void deleteAfterEndDate(Long houseWorkId);
}
