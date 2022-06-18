package com.depromeet.fairer.api.housework;

import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.dto.housework.request.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkStatusRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;

    @PostMapping("")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@RequestMemberId Long memberId,  @RequestBody @Valid HouseWorkListRequestDto req) {
        List<HouseWorkResponseDto> houseWorkList = houseWorkService.createHouseWorks(memberId, req.getHouseWorks());
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

    @ApiOperation(value = "날짜별 집안일 조회", notes = "해당 멤버의 팀원들에게 할당된 집안일까지 모두 조회")
    @GetMapping(value = "")
    public ResponseEntity<HouseWorkMemberResponseDto> getHouseWork(@RequestParam("scheduledDate") String scheduledDate,
                                                                   @RequestMemberId Long memberId){
        log.info("멤버아이디 확인" + String.valueOf(memberId));
        LocalDate scheduledDateParse = LocalDate.parse(scheduledDate, DateTimeFormatter.ISO_DATE);
        return ResponseEntity.ok(houseWorkService.getHouseWork(scheduledDateParse, memberId));
    }

    @ApiOperation(value = "개별 집안일 조회", notes = "")
    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId){
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    @ApiOperation(value = "집안일 완료 상태 변경", notes = "미완 -> 완료일 때 toBeStatus=1, 완료 -> 미완료일 때 toBeStatus=0")
    @PatchMapping(value = "{houseWorkId}")
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId, @RequestBody @Valid HouseWorkStatusRequestDto req){
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }

    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam(required = true) String scheduledDate, @RequestMemberId Long memberId) {
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate, memberId);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }
}