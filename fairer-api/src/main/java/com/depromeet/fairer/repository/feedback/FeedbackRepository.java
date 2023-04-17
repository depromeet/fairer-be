package com.depromeet.fairer.repository.feedback;

import com.depromeet.fairer.domain.feedback.Feedback;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long>, FeedbackCustomRepository {

    @Modifying(clearAutomatically = true)
    @Query("select f from Feedback f where f.houseworkComplete.houseWorkCompleteId =:houseWorkCompleteId")
    List<Feedback> findByHouseWorkCompleteId(@Param(value = "houseWorkCompleteId") Long houseWorkCompleteId);
}
