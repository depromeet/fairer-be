package com.depromeet.fairer.api;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.houseworkComplete.response.MemberHouseWorkStatisticDto;
import com.depromeet.fairer.dto.houseworkComplete.response.MonthlyHouseWorkStatisticResponseDto;
import com.depromeet.fairer.dto.statistic.request.MonthlyHouseWorkStatisticRequestDto;
import com.depromeet.fairer.dto.statistic.request.MonthlyRankingRequestDto;
import com.depromeet.fairer.dto.statistic.response.StatisticResponseDto;
import com.depromeet.fairer.global.resolver.RequestMemberId;
import com.depromeet.fairer.service.housework.HouseWorkService;
import com.depromeet.fairer.service.houseworkComplete.HouseWorkCompleteService;
import com.depromeet.fairer.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "statistics", description = "집안일 통계 API")
@RequestMapping("/api/statistics")
public class StatisticController {

    private final MemberService memberService;
    private final HouseWorkService houseWorkService;
    private final HouseWorkCompleteService houseWorkCompleteService;

    @Tag(name = "statistics")
    @ApiOperation(value = "통계 리스트 조회", notes = "yearMonth는 yyyy-MM로 보내주시면 됩니다.")
    @GetMapping("")
    public ResponseEntity<StatisticResponseDto.StatisticResponseDtoList> getStatisticList(@RequestParam String yearMonth,
                                                                                          @ApiIgnore @RequestMemberId Long memberId) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth date = YearMonth.parse(yearMonth, dateTimeFormatter);

        LocalDate today = LocalDate.of(date.getYear(), date.getMonth(), 1);
        Member member = memberService.find(memberId);

        // 한달 집안일 조회
        List<HouseWork> houseWorks = houseWorkService.getStatisticList(member.getTeam(), today);

        Map<String, Integer> statistic = new HashMap<>();
        int cnt = 0;
        for(HouseWork houseWork : houseWorks){

            // 완료 개수 조회
            int countDone = houseWorkCompleteService.getCompleteNum(houseWork.getHouseWorkId());

            if (statistic.containsKey(houseWork.getHouseWorkName())){
                cnt = statistic.get(houseWork.getHouseWorkName());
                statistic.put(houseWork.getHouseWorkName(), cnt + countDone);
            } else{
                statistic.put(houseWork.getHouseWorkName(), countDone);
            }

        }

        List<StatisticResponseDto> response = new ArrayList<>();
        statistic.forEach((name, count) -> {
            response.add(StatisticResponseDto.from(name, count));
        });

        return ResponseEntity.ok(new StatisticResponseDto.StatisticResponseDtoList(response));
    }

    @Tag(name = "statistics")
    @ApiOperation(value = "월별 집안일 완료 통계")
    @GetMapping("/team-member")
    public ResponseEntity<MonthlyHouseWorkStatisticResponseDto> getMonthlyHouseWorkStatistic(
            @Valid @ModelAttribute MonthlyHouseWorkStatisticRequestDto requestDto,
            @ApiIgnore @RequestMemberId Long memberId
    ) {
        MonthlyHouseWorkStatisticResponseDto monthlyHouseWorkStatisticResponseDto =
                houseWorkCompleteService.getMonthlyHouseWorkStatisticByMemberId(memberId, requestDto);
        return ResponseEntity.ok(monthlyHouseWorkStatisticResponseDto);
    }

    @Tag(name = "statistics")
    @ApiOperation(value = "월별 집안일 완료 랭킹", notes = "1등부터 차례대로 리턴")
    @GetMapping("/rank")
    public ResponseEntity<MonthlyHouseWorkStatisticResponseDto> getMonthlyHouseWorkRanking(
            @Valid @ModelAttribute MonthlyRankingRequestDto requestDto,
            @ApiIgnore @RequestMemberId Long memberId
    ) {
        MonthlyHouseWorkStatisticResponseDto monthlyHouseWorkStatisticResponseDto =
                houseWorkCompleteService.getMonthlyHouseWorkRanking(memberId, requestDto.getMonth());
        return ResponseEntity.ok(monthlyHouseWorkStatisticResponseDto);
    }

}
