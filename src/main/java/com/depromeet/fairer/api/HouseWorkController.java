package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.housework.HouseWork;

import com.depromeet.fairer.dto.housework.*;
import com.depromeet.fairer.service.HouseWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    /**
     * 날짜별 집안일 조회
     * @param scheduledDate 날짜
     * @return 날짜별 집안일 dto list
     */
    @GetMapping(value = "")
    public ResponseEntity<HouseWorkDateResponseDto> getHouseWorksTest(@RequestParam("scheduledDate") String scheduledDate){
        LocalDate scheduledDateParse = LocalDate.parse(scheduledDate, DateTimeFormatter.ISO_DATE);

        return ResponseEntity.ok(houseWorkService.getHouseWork(scheduledDateParse));
    }

    /**
     * 개별 집안일 조회
     * @param houseWorkId 집안일 id
     * @return 집안일 정보 dto
     */
    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorks(@PathVariable("houseWorkId") Long houseWorkId){
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    /**
     * 집안일 완료 상태 변경
     * @param houseWorkId 변경할 집안일 id
     * @return 변경된 집안일 상태
     */
    @PatchMapping(value = "{houseWorkId}")
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId,
                                                                            @RequestBody @Valid HouseWorkStatusRequestDto req){
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }

    /**
     * 공간 -> 집안일 프리셋 조회
     * @param space 공간
     * @return 집안일 이름 list
     */
    @GetMapping(value = "{space}")
    public ResponseEntity<HouseWorkPresetResponseDto> getHouseWorkPreset(@PathVariable String space){
        return ResponseEntity.ok(houseWorkService.getHouseWorkPreset(space));
    }

    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam(required = true) String scheduledDate) {
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }
}