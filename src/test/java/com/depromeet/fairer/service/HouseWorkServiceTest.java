package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.housework.Housework;
import com.depromeet.fairer.domain.housework.Space;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class HouseWorkServiceTest {
    @Autowired HouseWorkService houseWorkService;

    @Test
    @DisplayName("집안일 여러 개 일괄 생성")
    void createHouseWorks() {
        List<Housework> houseWorks = new ArrayList<Housework>(
                Arrays.asList(
                        Housework.builder()
                                .space(Space.LIVINGROOM)
                                .houseworkName("바닥 청소")
                                .scheduledDate(LocalDate.of(2022, 04, 24))
                                .scheduledTime(LocalTime.of(18, 00))
                                .success(false)
                                .build()
                        ,
                        Housework.builder()
                                .space(Space.KITCHEN)
                                .houseworkName("설거지")
                                .scheduledDate(LocalDate.of(2022, 04, 24))
                                .scheduledTime(LocalTime.of(20, 00))
                                .success(false)
                                .build()
                ));

        List<Housework> newHouseWorks = houseWorkService.createHouseWorks(houseWorks);
        Assertions.assertThat(newHouseWorks.get(0)).isEqualTo(houseWorks.get(0));
        Assertions.assertThat(newHouseWorks.get(1)).isEqualTo(houseWorks.get(1));
    }
}
