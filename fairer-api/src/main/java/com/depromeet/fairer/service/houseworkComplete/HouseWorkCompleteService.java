package com.depromeet.fairer.service.houseworkComplete;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.member.QMember;
import com.depromeet.fairer.dto.houseworkComplete.request.TeamHouseWorkStatisticThisMonthRequestDto;
import com.depromeet.fairer.dto.houseworkComplete.response.MemberHouseWorkStatisticDto;
import com.depromeet.fairer.dto.houseworkComplete.response.TeamHouseWorkStatisticPerMonthResponseDto;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.NoSuchMemberException;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.houseworkcomplete.HouseWorkCompleteRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.depromeet.fairer.domain.member.QMember.member;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HouseWorkCompleteService {

    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkCompleteRepository houseWorkCompleteRepository;
    private final MemberRepository memberRepository;

    public Long create(Long houseWorkId, LocalDate scheduledDate) {

        HouseWork houseWork = houseWorkRepository.findById(houseWorkId)
                .orElseThrow(() -> new EntityNotFoundException("houseworkId: " + houseWorkId + "에 해당하는 집안일을 찾을 수 없습니다."));

        HouseworkComplete complete = new HouseworkComplete(scheduledDate, houseWork, LocalDateTime.now());
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

    public TeamHouseWorkStatisticPerMonthResponseDto getTeamHouseWorkStatisticThisMonthByMemberId(
            Long memberId,
            TeamHouseWorkStatisticThisMonthRequestDto teamHouseWorkStatisticThisMonthRequestDto
    ) {
        Member currentMember = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchMemberException("memberId에 해당하는 회원을 찾지 못했습니다."));

        List<Tuple> teamHouseWorkStatistics = houseWorkCompleteRepository.getTeamHouseWorkStatisticThisMonthByTeamId(currentMember.getTeam().getTeamId(), teamHouseWorkStatisticThisMonthRequestDto.getYear(), teamHouseWorkStatisticThisMonthRequestDto.getMonth());

        List<MemberHouseWorkStatisticDto> houseWorkStatics = teamHouseWorkStatistics.stream().map(
                statistic -> {
                    Member member = statistic.get(QMember.member);
                    Long count = statistic.get(QMember.member.count());
                    return MemberHouseWorkStatisticDto.of(member, count);
                }
        ).toList();

        return TeamHouseWorkStatisticPerMonthResponseDto.of(houseWorkStatics);
    }

}
