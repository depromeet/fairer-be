package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.dto.housework.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.HouseWorkListResponseDto;
import com.depromeet.fairer.dto.housework.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.HouseWorkResponseDto;
import com.depromeet.fairer.service.HouseWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<Object> editHouseWork(@RequestBody @Valid HouseWorkRequestDto dto, @PathVariable Long id) {
        HouseWork houseWork = houseWorkService.updateHouseWork(id, dto);
        return new ResponseEntity<>(HouseWorkResponseDto.from(houseWork), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteHouseWork(@PathVariable Long id) {
        houseWorkService.deleteHouseWork(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}