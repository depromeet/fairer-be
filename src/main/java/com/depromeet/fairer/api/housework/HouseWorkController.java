package com.depromeet.fairer.api.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.dto.housework.request.HouseWorkListRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkStatusRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import com.depromeet.fairer.service.member.MemberService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;
    private final MemberService memberService;

    @ApiOperation(value = "집안일 생성 API ")
    @PostMapping("")
    public ResponseEntity<HouseWorkListResponseDto> createHouseWorks(@ApiIgnore @RequestMemberId Long memberId, @RequestBody @Valid HouseWorkListRequestDto dto) {
        List<HouseWorkResponseDto> houseWorkList = houseWorkService.createHouseWorks(memberId, dto.getHouseWorks());
        return new ResponseEntity<>(new HouseWorkListResponseDto(houseWorkList), HttpStatus.CREATED);
    }

    @ApiOperation(value = "집안일 수정 API ")
    @PutMapping("/{id}")
    public ResponseEntity<HouseWorkResponseDto> editHouseWork(
            @ApiIgnore @RequestMemberId Long memberId,
            @RequestBody @Valid HouseWorkRequestDto dto,
            @ApiParam(value = "수정할 집안일 ID", required = true) @PathVariable Long id) {
        return new ResponseEntity<>(houseWorkService.updateHouseWork(memberId, id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHouseWork(
            @ApiIgnore @RequestMemberId Long memberId,
            @ApiParam(value = "삭제할 집안일 ID", required = true) @PathVariable Long id) {
        houseWorkService.deleteHouseWork(memberId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "날짜별 집안일 조회", notes = "본인 포함 팀원들의 집안일까지 모두 조회")
    @GetMapping(value = "")
    public ResponseEntity<List<HouseWorkDateResponseDto>> getHouseWork(@RequestParam("scheduledDate") String scheduledDate,
                                                                       @ApiIgnore @RequestMemberId Long memberId) {
        LocalDate scheduledDateParse = LocalDate.parse(scheduledDate, DateTimeFormatter.ISO_DATE);

        List<Member> members = memberService.getMemberList(memberId);

        List<HouseWorkDateResponseDto> houseWorkDateResponseDtos = new ArrayList<>();
        for (Member member : members) {
            List<HouseWork> houseWorks = houseWorkService.getHouseWorks(scheduledDateParse, member);

            List<HouseWorkResponseDto> houseWorkResponseDtoList = houseWorks.stream().map(houseWork -> {
                List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(houseWork.getHouseWorkId())
                        .stream().map(MemberDto::from).collect(Collectors.toList());

                return HouseWorkResponseDto.from(houseWork, memberDtoList);
            }).collect(Collectors.toList());

            long countDone = houseWorkResponseDtoList.stream().filter(HouseWorkResponseDto::getSuccess).count();
            long countLeft = houseWorkResponseDtoList.stream().filter(houseWorkResponseDto -> !houseWorkResponseDto.getSuccess()).count();

            houseWorkDateResponseDtos.add(HouseWorkDateResponseDto.from(member.getMemberId(), scheduledDateParse, countDone, countLeft, houseWorkResponseDtoList));
        }

        return ResponseEntity.ok(houseWorkDateResponseDtos);
    }

    @ApiOperation(value = "개별 집안일 조회", notes = "")
    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId) {
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    @ApiOperation(value = "집안일 완료여부 수정", notes = "toBeStatus=0이면 완료->미완료, toBeStatus=1이면 미완료->완료")
    @PatchMapping(value = "{houseWorkId}")
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId, @RequestBody @Valid HouseWorkStatusRequestDto req) {
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }

    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam(required = true) String scheduledDate, @ApiIgnore @RequestMemberId Long memberId) {
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate, memberId);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }
}