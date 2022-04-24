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
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;

    @PostMapping("")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@RequestBody @Valid HouseWorkListRequestDto req) {
        List<Housework> houseWorks = houseWorkService.createHouseWorks(req.getHouseWorks());
        List<HouseWorkResponseDto> result = houseWorks.stream()
                .map(work -> new HouseWorkResponseDto(work))
                .collect(toList());

        return new ResponseEntity<HouseWorkListResponseDto>(new HouseWorkListResponseDto(result), HttpStatus.CREATED);
    }
}
