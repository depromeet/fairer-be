package com.depromeet.fairer.api.housework;

import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.dto.housework.request.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkStatusRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

    @ApiOperation(value = "집안일 생성 API ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, defaultValue = "authorization code", dataType = "String", value = "authorization code", required = true, paramType = "header")
    })
    @PostMapping("")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@RequestMemberId Long memberId, @RequestBody @Valid HouseWorkListRequestDto dto) {
        List<HouseWorkResponseDto> houseWorkList = houseWorkService.createHouseWorks(memberId, dto.getHouseWorks());
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

    @GetMapping(value = "")
    public ResponseEntity<HouseWorkMemberResponseDto> getHouseWork(@RequestParam("scheduledDate") String scheduledDate,
                                                                   @RequestMemberId Long memberId){
        LocalDate scheduledDateParse = LocalDate.parse(scheduledDate, DateTimeFormatter.ISO_DATE);
        return ResponseEntity.ok(houseWorkService.getHouseWork(scheduledDateParse, memberId));
    }

    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId) {
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    @PatchMapping(value = "{houseWorkId}")
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId, @RequestBody @Valid HouseWorkStatusRequestDto req) {
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }

    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam(required = true) String scheduledDate, @RequestMemberId Long memberId) {
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate, memberId);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }
}