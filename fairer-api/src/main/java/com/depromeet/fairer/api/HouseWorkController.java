package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.feedback.response.FeedbackCountResponseDto;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.service.feedback.FeedbackService;
import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.dto.housework.request.HouseWorksCreateRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkUpdateRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorkStatusRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import com.depromeet.fairer.service.member.MemberService;

import com.depromeet.fairer.service.team.TeamService;
import com.depromeet.fairer.dto.housework.request.HouseWorkDeleteRequestDto;
import com.depromeet.fairer.vo.houseWork.HouseWorkCompFeedbackVO;
import com.depromeet.fairer.vo.houseWork.HouseWorkUpdateVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "houseWorks", description = "집안일 API")
@RequestMapping("/api/houseworks")
public class HouseWorkController {
    private final HouseWorkService houseWorkService;
    private final MemberService memberService;
    private final TeamService teamService;
    private final FeedbackService feedbackService;
    private final ModelMapper modelMapper;

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 생성 API")
    @PostMapping("")
    public ResponseEntity<List<HouseWorkResponseDto>> createHouseWorks(@ApiIgnore @RequestMemberId Long memberId,
                                                                        @RequestBody @Valid List<HouseWorksCreateRequestDto> request) {
        List<HouseWorkResponseDto> responseDtos = new ArrayList<>();
        for (HouseWorksCreateRequestDto requestDto : request) {
            responseDtos.add(houseWorkService.createHouseWork(memberId, requestDto));
        }
        return new ResponseEntity<>(responseDtos, HttpStatus.CREATED);
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 수정 API - 반복 기능 구현 후")
    @PutMapping("/v2")
    public ResponseEntity<?> editHouseWork(@ApiIgnore @RequestMemberId Long memberId,
                                           @RequestBody @Valid HouseWorkUpdateRequestDto dto) {
        return ResponseEntity.ok(houseWorkService.updateHouseWork(memberId, dto));
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 삭제 API - 반복 기능 구현 후")
    @DeleteMapping("/v2")
    public ResponseEntity<?> deleteHouseWork(
            @ApiIgnore @RequestMemberId Long memberId,
            @RequestBody @Valid HouseWorkDeleteRequestDto dto) {
        houseWorkService.deleteHouseWork(memberId, dto.getHouseWorkId(), dto.getType(), dto.getDeleteStandardDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 쿼리 1개로 처리
    @Tag(name = "houseWorks")
    @ApiOperation(value = "팀원의 특정 기간 집안일 목록 조회 - 반복 기능 구현 후", notes = "본인이 속한 팀의 팀원의 특정 기간 집안일 목록 조회")
    @GetMapping("/list/member/{teamMemberId}/query")
    public ResponseEntity<Map<String, HouseWorkDateResponseDto>> getHouseWorkListByTeamMemberAndDateQuery(@RequestParam("fromDate") String fromDate,
                                                                                                          @RequestParam("toDate") String toDate,@PathVariable("teamMemberId") Long teamMemberId,
                                                                                                          @ApiIgnore @RequestMemberId Long memberId) {
        final LocalDate from = DateTimeUtils.stringToLocalDate(fromDate);
        final LocalDate to = DateTimeUtils.stringToLocalDate(toDate);

        teamService.checkJoinSameTeam(teamMemberId, memberId);
        Member teamMember = memberService.find(teamMemberId);

        Map<LocalDate, List<HouseWorkResponseDto>> results = new HashMap<>();
        Stream.iterate(from, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(from, to) + 1).forEach(date -> {

                    List<HouseWorkResponseDto> houseWorkResponseDtoList = houseWorkService.getHouseWorkByDateRepeatQuery(teamMember, date).stream().map(arr -> {
                        List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(arr.getHouseWork().getHouseWorkId())
                                .stream().map(MemberDto::from).collect(Collectors.toList());
                        return HouseWorkResponseDto.from(arr.getHouseWork(), memberDtoList, date, arr.getHouseWorkCompleteId());

                    }).collect(Collectors.toList());

                    results.put(date, houseWorkResponseDtoList);
                });

        return ResponseEntity.ok(makeHouseWorkListResponse(teamMemberId, results));
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "팀원의 특정 기간 집안일 목록 조회 - 피드백 기능 구현 후", notes = "본인이 속한 팀의 팀원의 특정 기간 집안일 목록 조회")
    @GetMapping("/list/member/{teamMemberId}/query/v2")
    public ResponseEntity<Map<String, HouseWorkDateResponseDtoV2>> getHouseWorkListByTeamMemberAndDateQueryV2(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @PathVariable("teamMemberId") Long teamMemberId, @ApiIgnore @RequestMemberId Long memberId) {

        teamService.checkJoinSameTeam(teamMemberId, memberId);
        Member teamMember = memberService.find(teamMemberId);

        Map<LocalDate, List<HouseWorkResponseDtoV2>> results = new HashMap<>();
        Stream.iterate(fromDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(fromDate, toDate) + 1).forEach(date -> {

                    List<HouseWorkResponseDtoV2> houseWorkResponseDtoList = houseWorkService.getHouseWorkByDateRepeatQuery(teamMember, date).stream().map(arr -> {
                        List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(arr.getHouseWork().getHouseWorkId())
                                .stream().map(MemberDto::from).collect(Collectors.toList());

                        return HouseWorkResponseDtoV2.from(arr.getHouseWork(), memberDtoList, date, arr.getHouseWorkCompleteId(), makeFeedbackCount(arr.getHouseWorkCompleteId()));
                    }).collect(Collectors.toList());

                    results.put(date, houseWorkResponseDtoList);
                });

        return ResponseEntity.ok(makeHouseWorkListResponseV2(teamMemberId, results));
    }

    // 팀 전체 쿼리 1개
    @Tag(name = "houseWorks")
    @ApiOperation(value = "특정 날짜별 집안일 조회 - 반복 기능 구현 후", notes = "특정 날짜별 집안일 조회")
    @GetMapping("/list/query")
    public ResponseEntity<Map<String, HouseWorkDateResponseDto>> getHouseWorkListByDateQuery(@RequestParam("fromDate") String fromDate,
                                                                                             @RequestParam("toDate") String toDate,
                                                                                             @ApiIgnore @RequestMemberId Long memberId) {
        final LocalDate from = DateTimeUtils.stringToLocalDate(fromDate);
        final LocalDate to = DateTimeUtils.stringToLocalDate(toDate);

        Member member = memberService.find(memberId);
        Map<LocalDate, List<HouseWorkResponseDto>> results = new HashMap<>();

        Stream.iterate(from, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(from, to) + 1).forEach(date -> {
                    List<HouseWorkResponseDto> houseWorkResponseDtoList = houseWorkService.getHouseWorkByDateRepeatTeamQuery(member.getTeam(), date).stream().map(arr -> {
                        List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(arr.getHouseWork().getHouseWorkId())
                                .stream().map(MemberDto::from).collect(Collectors.toList());
                        return HouseWorkResponseDto.from(arr.getHouseWork(), memberDtoList, date, arr.getHouseWorkCompleteId());
                    }).collect(Collectors.toList());

                    results.put(date, houseWorkResponseDtoList);
                });

        return ResponseEntity.ok(makeHouseWorkListResponse(memberId, results));
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "특정 날짜별 집안일 조회 - 피드백 기능 구현 후", notes = "특정 날짜별 집안일 조회")
    @GetMapping("/list/query/v2")
    public ResponseEntity<Map<String, HouseWorkDateResponseDtoV2>> getHouseWorkListByDateQueryV2(@RequestParam("fromDate") String fromDate,
                                                                                             @RequestParam("toDate") String toDate,
                                                                                             @ApiIgnore @RequestMemberId Long memberId) {
        final LocalDate from = DateTimeUtils.stringToLocalDate(fromDate);
        final LocalDate to = DateTimeUtils.stringToLocalDate(toDate);

        Member member = memberService.find(memberId);
        Map<LocalDate, List<HouseWorkResponseDtoV2>> results = new HashMap<>();

        Stream.iterate(from, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(from, to) + 1).forEach(date -> {
                    List<HouseWorkResponseDtoV2> houseWorkResponseDtoList = houseWorkService.getHouseWorkByDateRepeatTeamQuery(member.getTeam(), date).stream().map(arr -> {
                        List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(arr.getHouseWork().getHouseWorkId())
                                .stream().map(MemberDto::from).collect(Collectors.toList());

                       // return HouseWorkResponseDto.from(arr.getHouseWork(), memberDtoList, date, arr.getHouseWorkCompleteId());
                        return HouseWorkResponseDtoV2.from(arr.getHouseWork(), memberDtoList, date, arr.getHouseWorkCompleteId(), makeFeedbackCount(arr.getHouseWorkCompleteId()));
                    }).collect(Collectors.toList());

                    results.put(date, houseWorkResponseDtoList);
                });

        return ResponseEntity.ok(makeHouseWorkListResponseV2(memberId, results));
    }

    private Map<LocalDate, List<HouseWorkResponseDto>> getHouseWorkListGroupByScheduledDate(List<HouseWork> houseWorkList) {
        return houseWorkList.stream().map(houseWork -> {
            List<MemberDto> memberDtoList = memberService.getMemberListByHouseWorkId(houseWork.getHouseWorkId())
                    .stream().map(MemberDto::from).collect(Collectors.toList());
            return HouseWorkResponseDto.from(houseWork, memberDtoList);
        }).collect(Collectors.groupingBy(HouseWorkResponseDto::getScheduledDate, HashMap::new, Collectors.toCollection(ArrayList::new)));
    }

    private Map<String, HouseWorkDateResponseDto> makeHouseWorkListResponse(Long memberId, Map<LocalDate, List<HouseWorkResponseDto>> houseWorkListGroupByScheduledDate) {
        Map<String, HouseWorkDateResponseDto> response = new HashMap<>();
        houseWorkListGroupByScheduledDate.forEach((scheduledDate, houseWorkResponseDtoList) -> {
            long countDone = houseWorkResponseDtoList.stream().filter(HouseWorkResponseDto::getSuccess).count();
            long countLeft = houseWorkResponseDtoList.stream().filter(houseWorkResponseDto -> !houseWorkResponseDto.getSuccess()).count();
            response.put(DateTimeUtils.localDateToString(scheduledDate), HouseWorkDateResponseDto.from(memberId, scheduledDate, countDone, countLeft, houseWorkResponseDtoList));
        });
        return response;
    }

    private Map<String, HouseWorkDateResponseDtoV2> makeHouseWorkListResponseV2(Long memberId, Map<LocalDate, List<HouseWorkResponseDtoV2>> houseWorkListGroupByScheduledDate) {
        Map<String, HouseWorkDateResponseDtoV2> response = new HashMap<>();
        houseWorkListGroupByScheduledDate.forEach((scheduledDate, houseWorkResponseDtoList) -> {
            long countDone = houseWorkResponseDtoList.stream().filter(HouseWorkResponseDtoV2::getSuccess).count();
            long countLeft = houseWorkResponseDtoList.stream().filter(houseWorkResponseDto -> !houseWorkResponseDto.getSuccess()).count();
            response.put(DateTimeUtils.localDateToString(scheduledDate), HouseWorkDateResponseDtoV2.from(memberId, scheduledDate, countDone, countLeft, houseWorkResponseDtoList));
        });
        return response;
    }

    private FeedbackCountResponseDto makeFeedbackCount(Long houseWorkCompleteId){

        List<HouseWorkCompFeedbackVO> feedbackVOS= feedbackService.findAll(houseWorkCompleteId);
        final Map<Integer, Integer> emojiCount = new HashMap<>() {{
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
            put(6, 0);
        }};

        feedbackVOS.forEach(vo -> {
            if(vo.getComment() == null) {
                emojiCount.put(vo.getEmoji(), emojiCount.get(vo.getEmoji()) + 1);
            }
        });

        return FeedbackCountResponseDto.from(
                feedbackVOS.size(),
                emojiCount.get(1),
                emojiCount.get(2),
                emojiCount.get(3),
                emojiCount.get(4),
                emojiCount.get(5),
                emojiCount.get(6)
                );
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "개별 집안일 조회", notes = "")
    @GetMapping(value = "{houseWorkId}/detail")
    public ResponseEntity<HouseWorkResponseDto> getHouseWorkDetail(@PathVariable("houseWorkId") Long houseWorkId) {
        return ResponseEntity.ok(houseWorkService.getHouseWorkDetail(houseWorkId));
    }

    @Tag(name = "houseWorks")
    @GetMapping("/success/count")
    public ResponseEntity<HouseWorkSuccessCountResponseDto> getSuccessCount(@RequestParam String scheduledDate,
                                                                            @ApiIgnore @RequestMemberId Long memberId) {
        HouseWorkSuccessCountResponseDto houseWorkSuccessCountResponseDto = houseWorkService.getSuccessCount(scheduledDate, memberId);
        return ResponseEntity.ok(houseWorkSuccessCountResponseDto);
    }

    @Deprecated
    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 수정 API - 반복 기능 구현 전")
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

    @Deprecated
    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 삭제 API - 반복 기능 구현 전")
    @DeleteMapping("/{houseWorkId}")
    public ResponseEntity<?> deleteHouseWork(
            @ApiIgnore @RequestMemberId Long memberId,
            @ApiParam(value = "삭제할 집안일 ID", required = true) @PathVariable Long houseWorkId) {
        houseWorkService.deleteHouseWork(memberId, houseWorkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "날짜별 집안일 조회", notes = "본인 포함 팀원들의 집안일까지 모두 조회")
    @GetMapping(value = "")
    @Deprecated
    public ResponseEntity<List<HouseWorkDateResponseDto>> getHouseWork(@RequestParam("scheduledDate") String scheduledDate,
                                                                       @ApiIgnore @RequestMemberId Long memberId) {
        LocalDate scheduledDateParse = DateTimeUtils.stringToLocalDate(scheduledDate);

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

    // 1명 조회
    @Deprecated
    @Tag(name = "houseWorks")
    @ApiOperation(value = "팀원의 특정 기간 집안일 목록 조회 - 반복 기능 구현 전", notes = "본인이 속한 팀의 팀원의 특정 기간 집안일 목록 조회")
    @GetMapping("/list/member/{teamMemberId}")
    public ResponseEntity<Map<String, HouseWorkDateResponseDto>> getHouseWorkListByTeamMemberAndDate(@RequestParam("fromDate") String fromDate,
                                                                                 @RequestParam("toDate") String toDate,
                                                                                 @PathVariable("teamMemberId") Long teamMemberId,
                                                                                 @ApiIgnore @RequestMemberId Long memberId) {
        final LocalDate from = DateTimeUtils.stringToLocalDate(fromDate);
        final LocalDate to = DateTimeUtils.stringToLocalDate(toDate);

        teamService.checkJoinSameTeam(teamMemberId, memberId);

        Member teamMember = memberService.find(teamMemberId);
        List<HouseWork> houseWorkList = houseWorkService.getHouseWorkByDate(teamMember, from, to);
        Map<LocalDate, List<HouseWorkResponseDto>> houseWorkListGroupByScheduledDate = getHouseWorkListGroupByScheduledDate(houseWorkList);
        return ResponseEntity.ok(makeHouseWorkListResponse(teamMemberId, houseWorkListGroupByScheduledDate));
    }

    // 팀 전체 조회
    @Deprecated
    @Tag(name = "houseWorks")
    @ApiOperation(value = "특정 날짜별 집안일 조회 - 반복 기능 구현 전", notes = "특정 날짜별 집안일 조회")
    @GetMapping("/list")
    public ResponseEntity<Map<String, HouseWorkDateResponseDto>> getHouseWorkListByDate(@RequestParam("fromDate") String fromDate,
                                                                              @RequestParam("toDate") String toDate,
                                                                              @ApiIgnore @RequestMemberId Long memberId) {
        final LocalDate from = DateTimeUtils.stringToLocalDate(fromDate);
        final LocalDate to = DateTimeUtils.stringToLocalDate(toDate);

        Member member = memberService.find(memberId);
        List<HouseWork> houseWorkList = houseWorkService.getHouseWorkByDateAndTeam(member.getTeam(), from, to);
        Map<LocalDate, List<HouseWorkResponseDto>> houseWorkListGroupByScheduledDate = getHouseWorkListGroupByScheduledDate(houseWorkList);
        return ResponseEntity.ok(makeHouseWorkListResponse(memberId, houseWorkListGroupByScheduledDate));
    }

    @Tag(name = "houseWorks")
    @ApiOperation(value = "집안일 완료여부 수정 - 반복 기능 구현 전", notes = "toBeStatus=0이면 완료->미완료, toBeStatus=1이면 미완료->완료")
    @PatchMapping(value = "{houseWorkId}")
    @Deprecated
    public ResponseEntity<HouseWorkStatusResponseDto> updateHouseWorkStatus(@PathVariable("houseWorkId") Long houseWorkId,
                                                                            @RequestBody @Valid HouseWorkStatusRequestDto req) {
        return ResponseEntity.ok(houseWorkService.updateHouseWorkStatus(houseWorkId, req.getToBeStatus()));
    }
}