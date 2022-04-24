package com.depromeet.fairer.repository;

import com.depromeet.fairer.domain.housework.Housework;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HouseWorkRepository {
    private final EntityManager em;

    @Transactional
    public List<Housework> createBatch(List<Housework> houseWorks) {
        houseWorks.stream().forEach(houseWork -> em.persist(houseWork));
        return houseWorks;
    }
}
