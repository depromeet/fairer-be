package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, Long>, HouseWorkCustomRepository {
    List<HouseWork> findAllByScheduledDate(LocalDate scheduledDate);
}