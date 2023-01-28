package com.depromeet.fairer.repository.houseworkcomplete;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.querydsl.core.Tuple;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HouseWorkCompleteRepository extends JpaRepository<HouseworkComplete, Long>, HouseWorkCompleteCustomRepository {
    List<HouseworkComplete> findAllByHouseWorkAndScheduledDateGreaterThanEqual(HouseWork houseWork, LocalDate scheduledDate);
    HouseworkComplete findByHouseWorkAndScheduledDate(HouseWork houseWork, LocalDate scheduledDate);

    @Modifying(clearAutomatically = true)
    @Query("delete from HouseworkComplete c where c.houseWork.houseWorkId = :houseWorkId")
    void deleteAllByHouseworkId(@Param(value = "houseWorkId") Long houseWorkId);

    @Modifying(clearAutomatically = true)
    @Query("delete from HouseworkComplete c where c.scheduledDate = :scheduledDate and c.houseWork.houseWorkId = :houseWorkId")
    void deleteAllByHouseworkIdAndScheduledDate(@Param(value = "houseWorkId") Long houseWorkId, @Param(value = "scheduledDate") LocalDate scheduledDate);

    void deleteAllByHouseWorkAndScheduledDate(HouseWork houseWork, LocalDate scheduledDate);

    @Modifying(clearAutomatically = true)
    @Query("select c from HouseworkComplete c where c.houseWork.houseWorkId =:houseWorkId")
    List<HouseworkComplete> getCompleteList(@Param(value = "houseWorkId") Long houseWorkId);

    List<Tuple> getTeamHouseWorkStatisticPerMonthByTeamIdAndHouseWorkName(Long teamId, int year, int month, String houseWorkName);
}
