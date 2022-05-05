package com.depromeet.fairer.api.housework;

import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.dto.housework.request.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkStatusRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;

    @PostMapping("/")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@RequestBody @Valid HouseWorkListRequestDto req) {
        List<HouseWorkResponseDto> houseWorkList = houseWorkService.createHouseWorks(req.getHouseWorks());
        return new ResponseEntity<>(new HouseWorkListResponseDto(houseWorkList), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HouseWorkResponseDto> editHouseWork(@RequestBody @Valid HouseWorkRequestDto dto, @PathVariable Long id) {
        return new ResponseEntity<>(houseWorkService.updateHouseWork(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHouseWork(@PathVariable Long id) {
        houseWorkService.deleteHouseWork(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 날짜별 집안일 조회
     * @param scheduledDate 날짜
     * @return 날짜별 집안일 dto list
     */
    @GetMapping(value = "/")
    public ResponseEntity<HouseWorkDateResponseDto> getHouseWork(@RequestParam("scheduledDate") String scheduledDate){
        LocalDate scheduledDateParse = LocalDate.parse(scheduledDate, DateTimeFormatter.ISO_DATE);
        return ResponseEntity.ok(houseWorkService.getHouseWork(scheduledDateParse));
    }

    /**
     * 개별 집안일 조회
     * @param houseWorkId 집안일 id
     * @return 집안일 정보 dto
     */
    @GetMapping(value = "/{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId){
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    /**
     * 집안일 완료 상태 변경
     * @param houseWorkId 변경할 집안일 id
     * @return 변경된 집안일 상태
     */
    @PatchMapping(value = "/{houseWorkId}")
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId, @RequestBody @Valid HouseWorkStatusRequestDto req){
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }

    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam(required = true) String scheduledDate) {
        Long memberId = 3L;
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate, memberId);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }
}