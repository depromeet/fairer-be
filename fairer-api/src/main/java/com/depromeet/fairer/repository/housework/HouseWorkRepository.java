package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.team.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, Long>, HouseWorkCustomRepository {
    List<HouseWork> findAllByScheduledDateAndAssignmentsIn(LocalDate scheduledDate, List<Assignment> assignments);
    List<HouseWork> findAllByScheduledDateBetweenAndAssignmentsIn(LocalDate fromDate, LocalDate toDate, List<Assignment> assignments);
    List<HouseWork> findAllByScheduledDateBetweenAndTeam(LocalDate fromDate, LocalDate toDate, Team team);

    @EntityGraph(attributePaths = {"team"})
    Optional<HouseWork> findWithTeamByHouseWorkId(Long houseWorkId);
}