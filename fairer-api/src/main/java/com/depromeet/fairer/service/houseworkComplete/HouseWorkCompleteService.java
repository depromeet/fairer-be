package com.depromeet.fairer.service.houseworkComplete;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.statistic.request.MonthlyHouseWorkStatisticRequestDto;
import com.depromeet.fairer.dto.houseworkComplete.response.MemberHouseWorkStatisticDto;
import com.depromeet.fairer.dto.houseworkComplete.response.MonthlyHouseWorkStatisticResponseDto;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.NoSuchMemberException;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.houseworkcomplete.HouseWorkCompleteRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.vo.houseWorkComplete.HouseWorkCompleteStatisticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HouseWorkCompleteService {

    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkCompleteRepository houseWorkCompleteRepository;
    private final MemberRepository memberRepository;

    public Long create(Long houseWorkId, LocalDate scheduledDate, Long memberId) {

        HouseWork houseWork = houseWorkRepository.findById(houseWorkId)
                .orElseThrow(() -> new EntityNotFoundException("houseworkId: " + houseWorkId + "에 해당하는 집안일을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("memberId: " + memberId + "에 해당하는 멤버를 찾을 수 없습니다."));

        HouseworkComplete complete = new HouseworkComplete(scheduledDate, houseWork, LocalDateTime.now(), member);
        return houseWorkCompleteRepository.save(complete).getHouseWorkCompleteId();
    }

    public void delete(Long houseWorkCompleteId) {

        final HouseworkComplete houseworkComplete = houseWorkCompleteRepository.findById(houseWorkCompleteId).orElseThrow(() -> {
            throw new BadRequestException("완료되지 않은 집안일 입니다.");
        });

        houseWorkCompleteRepository.deleteById(houseWorkCompleteId);
    }

    public Integer getCompleteNum(Long houseWorkId){

        return houseWorkCompleteRepository.getCompleteList(houseWorkId).size();
    }

    public MonthlyHouseWorkStatisticResponseDto getMonthlyHouseWorkStatisticByMemberId(
            Long memberId,
            MonthlyHouseWorkStatisticRequestDto requestDto
    ) {

        Member currentMember = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchMemberException("memberId에 해당하는 회원을 찾지 못했습니다."));

        List<Member> members = memberRepository.findAllByTeam(currentMember.getTeam());

        List<MemberHouseWorkStatisticDto> result = new ArrayList<>();
        for(Member member : members){
            Long completeCount = (long) houseWorkCompleteRepository.findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkNameV2(
                    member.getMemberId(), YearMonth.from(requestDto.getMonth()), requestDto.getHouseWorkName()).size();
            result.add(MemberHouseWorkStatisticDto.of(member, completeCount));
        }

        return MonthlyHouseWorkStatisticResponseDto.of(result);
    }

    public MonthlyHouseWorkStatisticResponseDto getMonthlyHouseWorkRanking(Long memberId, LocalDate month) {

        Member currentMember = memberRepository.findById(memberId).orElseThrow(
                () -> new NoSuchMemberException("memberId에 해당하는 회원을 찾지 못했습니다.")
        );

        List<MemberHouseWorkStatisticDto> memberHouseWorkStatisticDtos = new ArrayList<>();
        for(Member member : currentMember.getTeam().getMembers()){
            memberHouseWorkStatisticDtos.add(
                    MemberHouseWorkStatisticDto.of(member, houseWorkCompleteRepository.getMonthlyCountByMember(member.getMemberId(), YearMonth.from(month))));
        }

//        List<HouseWorkCompleteStatisticsVo> teamHouseWorkStatistics = houseWorkCompleteRepository.findMonthlyHouseWorkRanking(
//                currentMember.getTeam().getTeamId(),
//                YearMonth.from(month));
//
//        List<MemberHouseWorkStatisticDto> houseWorkStatics = teamHouseWorkStatistics.stream()
//                .map(statistic -> MemberHouseWorkStatisticDto.of(statistic.getMember(), statistic.getCompleteCount()))
//                .sorted(Comparator.comparing(MemberHouseWorkStatisticDto::getHouseWorkCount).reversed())
//                .collect(Collectors.toList());

        memberHouseWorkStatisticDtos.sort(Comparator.comparing(MemberHouseWorkStatisticDto::getHouseWorkCount).reversed());
        return MonthlyHouseWorkStatisticResponseDto.of(memberHouseWorkStatisticDtos);
    }

}
