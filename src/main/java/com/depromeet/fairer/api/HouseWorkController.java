package com.depromeet.fairer.api;

import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.dto.housework.request.HouseWorksCreateRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkUpdateRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkStatusRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import com.depromeet.fairer.service.member.MemberService;

import com.depromeet.fairer.vo.houseWork.HouseWorkUpdateVo;
import com.depromeet.fairer.vo.houseWork.HouseWorkAndAssigneeResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "houseWorks", description = "집안일 API")
@RequestMapping("/api/houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 생성 API ")
    @PostMapping("")
    public ResponseEntity<HouseWorksCreateResponseDto> createHouseWorks(@ApiIgnore @RequestMemberId Long memberId, @RequestBody @Valid HouseWorksCreateRequestDto dto) {
        List<HouseWorkResponseDto> houseWorkList = houseWorkService.createHouseWorks(memberId, dto.getHouseWorks());
        return new ResponseEntity<>(new HouseWorksCreateResponseDto(houseWorkList), HttpStatus.CREATED);
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 수정 API")
    @PutMapping("/{houseWorkId}")
    public ResponseEntity<HouseWorkResponseDto> editHouseWork(@ApiIgnore @RequestMemberId Long memberId,
                                                              @RequestBody @Valid HouseWorkUpdateRequestDto dto,
                                                              @ApiParam(value = "수정할 집안일 ID", required = true) @PathVariable Long houseWorkId) {
        final HouseWorkUpdateVo houseWorkUpdateVo = new HouseWorkUpdateVo();
        modelMapper.map(dto, houseWorkUpdateVo);
        houseWorkUpdateVo.setMemberId(memberId);
        houseWorkUpdateVo.setHouseWorkId(houseWorkId);
        return new ResponseEntity<>(houseWorkService.updateHouseWork(houseWorkUpdateVo), HttpStatus.OK);
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 삭제 API ")
    @DeleteMapping("/{houseWorkId}")
    public ResponseEntity<?> deleteHouseWork(
            @ApiIgnore @RequestMemberId Long memberId,
            @ApiParam(value = "삭제할 집안일 ID", required = true) @PathVariable Long houseWorkId) {
        houseWorkService.deleteHouseWork(memberId, houseWorkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "특정 멤버의 날짜별 집안일 조회", notes = "특정 멤버의 날짜별 집안일 조회")
    @GetMapping("")
    public ResponseEntity<HouseWorkAndAssigneeResponseDto> getTheMemberHouseWork(@RequestParam("reqDate") String reqDate,
                                                                                 @RequestParam("memberId") Long memberId,
                                                                                 @ApiIgnore @RequestMemberId Long reqMemberId) {
        final LocalDate localDate = DateTimeUtils.stringToLocalDate(reqDate);
        final HouseWorkAndAssigneeResponseDto theMemberHouseWorks = houseWorkService.getTheMemberHouseWorks(reqMemberId, memberId, localDate);
        return ResponseEntity.ok(theMemberHouseWorks);
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "특정 날짜별 집안일 조회", notes = "특정 날짜별 집안일 조회")
    @GetMapping("/date")
    public ResponseEntity<HouseWorkAndAssigneeResponseDto> getHouseWorkByDate(@RequestParam("reqDate") String reqDate, @ApiIgnore @RequestMemberId Long reqMemberId) {
        final LocalDate localDate = DateTimeUtils.stringToLocalDate(reqDate);
        final HouseWorkAndAssigneeResponseDto responseDto = houseWorkService.getHouseWorkByDate(reqMemberId, localDate);
        return ResponseEntity.ok(responseDto);
    }

//    @Tag(name = "houseWorks")
//    @ApiOperation(value = "날짜별 집안일 조회", notes = "본인 포함 팀원들의 집안일까지 모두 조회")
//    @GetMapping(value = "")
//    public ResponseEntity<List<HouseWorkDateResponseDto>> getHouseWork(@RequestParam("reqDate") String reqDate,
//                                                                       @ApiIgnore @RequestMemberId Long memberId) {
//        final LocalDate reqDateParse = DateTimeUtils.stringToLocalDate(reqDate);
//
//        houseWorkService.getMyTeamAllHouseWorksByDate(memberId, reqDate);
//
//        List<Member> members = memberService.getMyTeamMembers(memberId);
//
//        List<HouseWorkDateResponseDto> houseWorkDateResponseDtos = new ArrayList<>();
//        for (Member member : members) {
//            List<HouseWork> houseWorks = houseWorkService.getHouseWorks(reqDateParse, member);
//
//            List<HouseWorkResponseDto> houseWorkResponseDtoList = houseWorks.stream().map(houseWork -> {
//                List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(houseWork.getHouseWorkId())
//                        .stream().map(MemberDto::from).collect(Collectors.toList());
//
//                return HouseWorkResponseDto.from(houseWork, memberDtoList);
//            }).collect(Collectors.toList());
//
//            long countDone = houseWorkResponseDtoList.stream().filter(HouseWorkResponseDto::getSuccess).count();
//            long countLeft = houseWorkResponseDtoList.stream().filter(houseWorkResponseDto -> !houseWorkResponseDto.getSuccess()).count();
//
//            houseWorkDateResponseDtos.add(HouseWorkDateResponseDto.from(member.getMemberId(), reqDateParse, countDone, countLeft, houseWorkResponseDtoList));
//        }
//
//        return ResponseEntity.ok(houseWorkDateResponseDtos);
//    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "개별 집안일 조회", notes = "")
    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId) {
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 완료여부 수정", notes = "toBeStatus=0이면 완료->미완료, toBeStatus=1이면 미완료->완료")
    @PatchMapping(value = "{houseWorkId}")
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId, @RequestBody @Valid HouseWorkStatusRequestDto req) {
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }

    @Tag(name = "houseWorks")
    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam(required = true) String scheduledDate, @ApiIgnore @RequestMemberId Long memberId) {
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate, memberId);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }
}