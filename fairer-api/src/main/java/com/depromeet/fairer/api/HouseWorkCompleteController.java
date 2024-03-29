package com.depromeet.fairer.api;

import com.depromeet.fairer.dto.houseworkComplete.response.HouseWorkCompleteResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.service.houseworkComplete.HouseWorkCompleteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "houseWorkComplete", description = "집안일 완료 API")
@RequestMapping("/api/houseworks/complete")
public class HouseWorkCompleteController {

    private final HouseWorkCompleteService houseWorkCompleteService;

    @Tag(name = "houseWorkComplete")
    @ApiOperation(value = "집안일 완료 생성 - 반복 기능 구현 후")
    @PostMapping(value = "/{houseWorkId}")
    public ResponseEntity<HouseWorkCompleteResponseDto> createHouseWorkComp(@ApiIgnore @RequestMemberId Long memberId,
                                                                            @PathVariable("houseWorkId") Long houseWorkId,
                                                                            @RequestParam("scheduledDate") String scheduledDate) {
        final LocalDate date = DateTimeUtils.stringToLocalDate(scheduledDate);

        final Long houseWorkCompleteId = houseWorkCompleteService.create(houseWorkId, date, memberId);
        return new ResponseEntity<>(HouseWorkCompleteResponseDto.create(houseWorkCompleteId), HttpStatus.CREATED);
    }

    @Tag(name = "houseWorkComplete")
    @ApiOperation(value = "집안일 완료 삭제 - 반복 기능 구현 후")
    @DeleteMapping("/{houseWorkCompleteId}")
    public ResponseEntity<?> deleteHouseWorkComp(@PathVariable("houseWorkCompleteId") Long houseWorkCompleteId) {
        houseWorkCompleteService.delete(houseWorkCompleteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}