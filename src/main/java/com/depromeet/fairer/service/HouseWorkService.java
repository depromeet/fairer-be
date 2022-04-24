package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.housework.Housework;
import com.depromeet.fairer.repository.HouseWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseWorkService {
    private final HouseWorkRepository houseWorkRepository;

    @Transactional
    public List<Housework> createHouseWorks(List<Housework> houseWorksDto) {
        return houseWorkRepository.createBatch(houseWorksDto);
    }
}
