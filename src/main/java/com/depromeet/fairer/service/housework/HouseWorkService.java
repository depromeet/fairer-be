package com.depromeet.fairer.service.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.housework.request.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.MemberTokenNotFoundException;
import com.depromeet.fairer.repository.assignment.AssignmentRepository;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.service.member.MemberService;
import com.depromeet.fairer.repository.team.TeamRepository;
import com.depromeet.fairer.service.member.MemberService;
import com.depromeet.fairer.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseWorkService {
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;
    private final AssignmentRepository assignmentRepository;
    private final MemberService memberService;
    private final TeamService teamService;

    @Transactional
    public List<HouseWorkResponseDto> createHouseWorks(Long memberId, List<HouseWorkRequestDto> houseWorksDto) {
        List<HouseWorkResponseDto> houseWorks = new ArrayList<>();
        for (HouseWorkRequestDto houseWorkDto : houseWorksDto) {
            houseWorks.add(this.createHouseWork(memberId, houseWorkDto));
        }
        return houseWorks;
    }

    private HouseWorkResponseDto createHouseWork(Long memberId, HouseWorkRequestDto houseWorkRequestDto) {
        HouseWork houseWork = houseWorkRequestDto.toEntity();
        Member member = memberService.findWithTeam(memberId);
        Team team = member.getTeam();
        if (team == null) {
            throw new BadRequestException("그룹에 소속되어있지 않아 집안일을 생성할 수 없습니다.");
        }

        houseWork.setTeam(team);
        houseWorkRepository.save(houseWork);

        List<Long> assignees = new ArrayList<>(houseWorkRequestDto.getAssignees());
        List<Member> members = memberRepository.findAllById(assignees);
        for (Member m : members) {
            Assignment assignment = Assignment.builder().houseWork(houseWork).member(m).build();
            assignmentRepository.save(assignment);
        }

        List<MemberDto> memberDtoList = members.stream().map(MemberDto::from).collect(Collectors.toList());
        return HouseWorkResponseDto.from(houseWork, memberDtoList);
    }

    @Transactional
    public HouseWorkResponseDto updateHouseWork(Long id, HouseWorkRequestDto dto) {
        HouseWork houseWork = getHouseWorkById(id);
        houseWork.setSpace(dto.getSpace());
        houseWork.setHouseWorkName(dto.getHouseWorkName());
        houseWork.setScheduledDate(dto.getScheduledDate());
        houseWork.setScheduledTime(dto.getScheduledTime());
        houseWorkRepository.save(houseWork);

//        TODO: Team 기능 도입되어 assignees 지정 가능한 경우 주석 해제
//        List<Member> members = memberRepository.findAllById(dto.getAssignees());

//        List<Assignment> assignments = assignmentRepository.findAllByHouseWorkAndMemberNotIn(houseWork, members);
//        assignmentRepository.deleteAll(assignments);

//        for (Member member : members) {
//            Optional<Assignment> assignment = assignmentRepository.findByHouseWorkAndMember(houseWork, member);
//            if(assignment.isEmpty()) {
//                assignmentRepository.save(Assignment.builder().houseWork(houseWork).member(member).build());
//            }
//        }

        List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(id).stream().map(MemberDto::from).collect(Collectors.toList());
        return HouseWorkResponseDto.from(houseWork, memberDtoList);
    }

    @Transactional
    public void deleteHouseWork(Long id) {
        try{
            houseWorkRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 집안일 입니다.");
        }
    }

    public HouseWorkSuccessCountResponseDto getSuccessCount(String scheduledDate, Long memberId) {
        LocalDate startDate = LocalDate.parse(scheduledDate).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate endDate = LocalDate.parse(scheduledDate).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusWeeks(1);
        Long count = houseWorkRepository.getHouseWorkSuccessCount(memberId, startDate, endDate);
        return HouseWorkSuccessCountResponseDto.of(count);
    }

    @Transactional
    public HouseWorkMemberResponseDto getHouseWork(LocalDate scheduledDate, Long memberId){
        Team team = teamService.getTeam(memberId);

        List<Member> memberList = memberRepository.findAllByTeam(Optional.ofNullable(team));

        List<HouseWorkDateResponseDto> houseWorkDateResponseDtos = new ArrayList<>();
        for (Member memberr : memberList){
            List<Assignment> assignmentList = assignmentRepository.findAllByMember(memberr);
            List<HouseWork> houseWorkList = houseWorkRepository.findAllByScheduledDateAndAssignmentsIn(scheduledDate, assignmentList);

            List<HouseWorkResponseDto> houseWorkResponseDtoList = houseWorkList.stream().map(houseWork -> {
                List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(houseWork.getHouseWorkId()).stream().map(MemberDto::from).collect(Collectors.toList());

                return HouseWorkResponseDto.from(houseWork, memberDtoList);
            }).collect(Collectors.toList());

            long countDone = houseWorkResponseDtoList.stream().filter(HouseWorkResponseDto::getSuccess).count();
            long countLeft = houseWorkResponseDtoList.stream().filter(houseWorkResponseDto -> !houseWorkResponseDto.getSuccess()).count();

            houseWorkDateResponseDtos.add(HouseWorkDateResponseDto.from(memberr.getMemberId(), scheduledDate, countDone, countLeft, houseWorkResponseDtoList));
        }

        return HouseWorkMemberResponseDto.from(team.getTeamId(), houseWorkDateResponseDtos);
    }

    @Transactional
    public HouseWorkResponseDto getHouseWorkDetail(Long houseWorkId) {
        HouseWork houseWork = getHouseWorkById(houseWorkId);
        List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(houseWorkId).stream().map(MemberDto::from).collect(Collectors.toList());
        return HouseWorkResponseDto.from(houseWork, memberDtoList);
    }

    @Transactional
    public HouseWorkStatusResponseDto updateHouseWorkStatus(Long houseWorkId,
                                                            int toBeStatus) {
        boolean status = toBeStatus == 1;
        HouseWork houseWork = getHouseWorkById(houseWorkId);
        houseWork.setSuccessDateTime(status ? LocalDateTime.now() : null);
        houseWork.setSuccess(status);
        houseWorkRepository.save(houseWork);
        return new HouseWorkStatusResponseDto(houseWorkId, status);
    }

    public HouseWork getHouseWorkById(Long houseWorkId) {
        return houseWorkRepository.findById(houseWorkId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 집안일 입니다."));
    }
}