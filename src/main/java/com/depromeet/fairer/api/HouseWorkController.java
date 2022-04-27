package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.housework.Housework;
import com.depromeet.fairer.dto.housework.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.HouseWorkListResponseDto;
import com.depromeet.fairer.dto.housework.HouseWorkResponseDto;
import com.depromeet.fairer.service.HouseWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;

    @PostMapping("")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@RequestBody @Valid HouseWorkListRequestDto req) {
        Iterable<Housework> HouseWorks = houseWorkService.createHouseWorks(req.getHouseWorks());

        List<HouseWorkResponseDto> houseWorkList = new ArrayList<>();
        HouseWorks.forEach(houseWork -> houseWorkList.add(HouseWorkResponseDto.from(houseWork)));
        return new ResponseEntity<>(new HouseWorkListResponseDto(houseWorkList), HttpStatus.CREATED);
    }
}
