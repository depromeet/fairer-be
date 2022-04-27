package com.depromeet.fairer.repository;

import com.depromeet.fairer.domain.housework.Housework;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HouseWorkRepository extends CrudRepository<Housework, Long> {
}

