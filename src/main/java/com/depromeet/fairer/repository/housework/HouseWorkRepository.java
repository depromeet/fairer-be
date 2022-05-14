package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, Long>, HouseWorkCustomRepository {
    List<HouseWork> findAllByScheduledDate(LocalDate scheduledDate);
    List<HouseWork> findAllByScheduledDateAndAssignmentsEq(LocalDate scheduledDate, List<Assignment> assignments);
}