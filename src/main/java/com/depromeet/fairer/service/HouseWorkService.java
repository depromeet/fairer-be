package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.dto.housework.HouseWorkRequestDto;
import com.depromeet.fairer.repository.HouseWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseWorkService {
    private final HouseWorkRepository houseWorkRepository;

    @Transactional
    public Iterable<HouseWork> createHouseWorks(List<HouseWorkRequestDto> houseWorksDto) {
        List<HouseWork> houseworkList = houseWorksDto.stream()
                .map(houseWorkDto -> HouseWork.builder()
                        .space(houseWorkDto.getSpace())
                        .houseWorkName(houseWorkDto.getHouseWorkName())
                        .scheduledDate(houseWorkDto.getScheduledDate())
                        .scheduledTime(houseWorkDto.getScheduledTime())
                        .success(false)
                        .successDateTime(null)
                        .build()
                ).collect(Collectors.toList());

        return houseWorkRepository.saveAll(houseworkList);
    }
}
