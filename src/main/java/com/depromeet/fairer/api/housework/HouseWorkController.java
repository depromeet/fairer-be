package com.depromeet.fairer.api.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.dto.housework.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.HouseWorkListResponseDto;
import com.depromeet.fairer.dto.housework.HouseWorkResponseDto;
import com.depromeet.fairer.service.housework.HouseWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;

    @PostMapping("")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@RequestBody @Valid HouseWorkListRequestDto req) {
        Iterable<HouseWork> HouseWorks = houseWorkService.createHouseWorks(req.getHouseWorks());

        List<HouseWorkResponseDto> houseWorkList = new ArrayList<>();
        HouseWorks.forEach(houseWork -> houseWorkList.add(HouseWorkResponseDto.from(houseWork)));
        return new ResponseEntity<>(new HouseWorkListResponseDto(houseWorkList), HttpStatus.CREATED);
    }
}