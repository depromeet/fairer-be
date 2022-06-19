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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;
    private final MemberService memberService;

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

    @GetMapping(value = "")
    public ResponseEntity<List<HouseWorkDateResponseDto>> getHouseWork(@RequestParam("scheduledDate") String scheduledDate,
                                                                   @RequestMemberId Long memberId){
        LocalDate scheduledDateParse = LocalDate.parse(scheduledDate, DateTimeFormatter.ISO_DATE);

        List<Member> members = memberService.getMemberList(memberId);

        List<HouseWorkDateResponseDto> houseWorkDateResponseDtos = new ArrayList<>();
        for (Member member : members){
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

    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId){
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

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