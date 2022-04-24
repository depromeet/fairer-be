package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.housework.Housework;
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
    public List<Housework> createHouseWorks(List<HouseWorkRequestDto> houseWorksDto) {
        List<Housework> houseworkList = houseWorksDto.stream()
                .map(houseWorkDto -> Housework.builder()
                        .space(houseWorkDto.getSpace())
                        .houseworkName(houseWorkDto.getHouseworkName())
                        .scheduledDate(houseWorkDto.getScheduledDate())
                        .scheduledTime(houseWorkDto.getScheduledTime())
                        .success(houseWorkDto.getSuccess())
                        .successDateTime(houseWorkDto.getSuccessDateTime())
                        .build()
                ).collect(Collectors.toList());

        return houseWorkRepository.createBatch(houseworkList);
    }
}
