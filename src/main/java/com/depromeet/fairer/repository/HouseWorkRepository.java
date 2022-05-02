package com.depromeet.fairer.repository;

import com.depromeet.fairer.domain.housework.HouseWork;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HouseWorkRepository extends CrudRepository<HouseWork, Long>, HouseWorkCustomRepository {

    @Query("select a.member.memberId from Assignment a left join a.housework on a.housework.houseWorkId=:houseWorkId")
    List<Long> findMemberByHouseWorkId(@Param("houseWorkId") Long houseWorkId);

    @Query("select hw.houseWorkId from HouseWork hw where hw.scheduledDate=:scheduledDate")
    List<Long> findHouseWorkIdByDate(@Param("scheduledDate") LocalDate scheduledDate);

    //countDone
    @Query("select count(hw.success) as countDone from HouseWork hw where hw.scheduledDate=:scheduledDate and hw.success=true")
    int countDone(@Param("scheduledDate") LocalDate scheduledDate);

    //countLeft
    @Query("select count(hw.success) as countDone from HouseWork hw where hw.scheduledDate=:scheduledDate and hw.success=false")
    int countLeft(@Param("scheduledDate") LocalDate scheduledDate);

    @Modifying
    @Query("update HouseWork hw set hw.success = 1 where hw.houseWorkId=:houseWorkId")
    int updateStatusTrue(@Param("houseWorkId") Long houseWorkId);

    @Modifying
    @Query("update HouseWork hw set hw.success = 0 where hw.houseWorkId=:houseWorkId")
    int updateStatusFalse(@Param("houseWorkId") Long houseWorkId);

    @Query("select p.presetHouseWorkName from Preset p where p.presetSpaceName=:space")
    List<String> getHouseWorkPreset(@Param("space") String space);
}