package com.depromeet.fairer.repository.houseworkcomplete;

import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface HouseWorkCompleteRepository extends JpaRepository<HouseworkComplete, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from HouseworkComplete c where c.houseWork.houseWorkId = :houseWorkId")
    void deleteAllByHouseworkId(@Param(value = "houseWorkId") Long houseWorkId);

    @Modifying(clearAutomatically = true)
    @Query("delete from HouseworkComplete c where c.scheduledDate = :scheduledDate and c.houseWork.houseWorkId = :houseWorkId")
    void deleteAllByHouseworkIdAndScheduledDate(@Param(value = "houseWorkId") Long houseWorkId, @Param(value = "scheduledDate") LocalDate scheduledDate);
}
