package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, Long>, HouseWorkCustomRepository {
    List<HouseWork> findAllByScheduledDateAndAssignmentsIn(LocalDate scheduledDate, List<Assignment> assignments);

//    @EntityGraph(attributePaths = {"assignments"})
//    List<HouseWork> findHouseWorksByMember(Member member);
}