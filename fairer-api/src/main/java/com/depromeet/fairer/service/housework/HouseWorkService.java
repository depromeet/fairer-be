package com.depromeet.fairer.service.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.housework.constant.RepeatCycle;
import com.depromeet.fairer.domain.housework.constant.UpdateDeletePolicyType;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.repeatexception.RepeatException;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.housework.request.HouseWorkUpdateRequestDto;
import com.depromeet.fairer.dto.housework.request.HouseWorksCreateRequestDto;
import com.depromeet.fairer.dto.housework.response.*;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.PermissionDeniedException;
import com.depromeet.fairer.repository.assignment.AssignmentRepository;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.houseworkcomplete.HouseWorkCompleteRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.repository.repeatexception.RepeatExceptionRepository;
import com.depromeet.fairer.service.assignment.AssignmentService;
import com.depromeet.fairer.service.member.MemberService;
import com.depromeet.fairer.service.team.TeamService;
import com.depromeet.fairer.vo.houseWork.HouseWorkUpdateVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HouseWorkService {
    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkCompleteRepository houseWorkCompleteRepository;
    private final MemberRepository memberRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentService assignmentService;
    private final MemberService memberService;
    private final TeamService teamService;
    private final RepeatExceptionRepository repeatExceptionRepository;

    public HouseWorkResponseDto createHouseWork(Long memberId, HouseWorksCreateRequestDto requestDto) {
        final Team team = teamService.getTeam(memberId);
        final HouseWork houseWork = requestDto.toEntity();
        houseWork.setTeam(team);
        houseWorkRepository.save(houseWork);

        List<Member> members = memberRepository.findAllById(requestDto.getAssignees());
        assignmentService.saveAssignments(members, houseWork);
        return HouseWorkResponseDto.from(houseWork,
                members.stream().map(MemberDto::from).collect(Collectors.toList()));
    }

    public HouseWorkResponseDto updateHouseWork(HouseWorkUpdateVo houseWorkUpdateVo) {
        Member member = memberService.findWithTeam(houseWorkUpdateVo.getMemberId());
        HouseWork houseWork = getHouseWorkById(houseWorkUpdateVo.getHouseWorkId());
        if (member.getTeam() != houseWork.getTeam()) {
            throw new PermissionDeniedException("집안일을 수정할 권한이 없습니다.");
        }

        List<Member> newAssignees = memberRepository.findAllById(houseWorkUpdateVo.getAssignees());
        Set<Long> teamMemberIds = houseWork.getTeam().getMembers().stream().map(Member::getMemberId).collect(Collectors.toSet());
        Set<Member> assigneesNotInTeam = newAssignees.stream().filter(assignee -> !teamMemberIds.contains(assignee.getMemberId())).collect(Collectors.toSet());
        if (assigneesNotInTeam.size() > 0) {
            throw new BadRequestException("같은 팀에 소속되지 않은 멤버를 집안일 담당자로 지정할 수 없습니다.");
        }

        houseWork.setSpace(houseWorkUpdateVo.getSpace());
        houseWork.setHouseWorkName(houseWorkUpdateVo.getHouseWorkName());
        houseWork.setScheduledDate(houseWorkUpdateVo.getScheduledDate());
        houseWork.setScheduledTime(houseWorkUpdateVo.getScheduledTime());
        houseWorkRepository.save(houseWork);

        List<Assignment> assignments = assignmentRepository.findAllByHouseWorkAndMemberNotIn(houseWork, newAssignees);
        assignmentRepository.deleteAll(assignments);

        for (Member assignee : newAssignees) {
            Optional<Assignment> assignment = assignmentRepository.findByHouseWorkAndMember(houseWork, assignee);
            if (assignment.isEmpty()) {
                assignmentRepository.save(Assignment.builder().houseWork(houseWork).member(assignee).build());
            }
        }

        List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(houseWorkUpdateVo.getHouseWorkId()).stream().map(MemberDto::from).collect(Collectors.toList());
        return HouseWorkResponseDto.from(houseWork, memberDtoList);
    }

    // 반복 기능 구현 전 version -> deprecated
    public void deleteHouseWork(Long memberId, Long houseWorkId) {
        HouseWork houseWork = findWithTeam(houseWorkId);
        Member member = memberService.findWithTeam(memberId);
        if (member.getTeam() != houseWork.getTeam()) {
            throw new PermissionDeniedException("집안일을 삭제할 권한이 없습니다.");
        }
        houseWorkRepository.deleteById(houseWorkId);
    }

    // 반복 기능 구현 후 version
    public void deleteHouseWork(Long memberId, Long houseWorkId, @NotNull String type, LocalDate deleteStandardDate) {
        HouseWork houseWork = findWithTeam(houseWorkId);
        Member member = memberService.findWithTeam(memberId);
        if (member.getTeam() != houseWork.getTeam()) {
            throw new PermissionDeniedException("집안일을 삭제할 권한이 없습니다.");
        }

        final RepeatCycle repeatCycle = houseWork.getRepeatCycle();
        if (repeatCycle == RepeatCycle.ONCE) { // 당일 일정일 경우 단순 삭제
            houseWorkRepository.deleteById(houseWork.getHouseWorkId()); // cascade 옵션으로 houseworkComplete, repeatException 까지 삭제
        } else { // 반복 일정일 경우 정책에 따라 삭제
            if (!houseWork.isIncludingDate(deleteStandardDate)) {
                throw new InvalidParameterException("요청한 삭제 날짜가 반복 주기에 포함되지 않습니다.");
            }
            switch (UpdateDeletePolicyType.of(type)) {
                case ALL: // 해당 반복 일정 모두 삭제
                    deleteAllHouseWork(houseWorkId);
                    break;
                case HEREAFTER: // 해당 반복 일정 중 오늘 포함 이후 삭제
                    houseWork.updateRepeatEndDateByCycle(deleteStandardDate);
                    repeatExceptionRepository.deleteAfterEndDate(houseWorkId);
                    break;
                case ONLY: // 해당 반복 일정 중 오늘만 삭제
                    repeatExceptionRepository.save(RepeatException.create(houseWork, deleteStandardDate));
                    houseWorkCompleteRepository.deleteAllByHouseWorkAndScheduledDate(houseWork, deleteStandardDate);
                    break;
            }
        }
    }

    private void deleteAllHouseWork(Long houseWorkId) {
        houseWorkRepository.deleteById(houseWorkId);
        assignmentRepository.deleteAllByHouseworkId(houseWorkId);
        //houseWorkCompleteRepository.deleteAllByHouseworkId(houseWorkId);
    }

    public HouseWork findWithTeam(Long houseWorkId) {
        return houseWorkRepository.findWithTeamByHouseWorkId(houseWorkId)
                .orElseThrow(() -> new EntityNotFoundException("houseworkId: " + houseWorkId + "에 해당하는 집안일을 찾을 수 없습니다."));
    }

    public HouseWorkSuccessCountResponseDto getSuccessCount(String scheduledDate, Long memberId) {
        LocalDate startDate = LocalDate.parse(scheduledDate).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = LocalDate.parse(scheduledDate).with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        Long count = houseWorkRepository.getHouseWorkSuccessCount(memberId, startDate, endDate);
        return HouseWorkSuccessCountResponseDto.of(count);
    }

    @Deprecated
    public List<HouseWork> getHouseWorks(LocalDate scheduledDate, Member member) {
        List<Assignment> assignmentList = assignmentRepository.findAllByMember(member);
        List<HouseWork> houseWorkList = houseWorkRepository.findAllByScheduledDateAndAssignmentsIn(scheduledDate, assignmentList);

        return houseWorkList;
    }

    public List<HouseWork> getHouseWorkByDate(Member member, LocalDate fromDate, LocalDate toDate) {
        List<Assignment> assignmentList = assignmentRepository.findAllByMember(member);
        return houseWorkRepository.findAllByScheduledDateBetweenAndAssignmentsIn(fromDate, toDate, assignmentList);
    }

    public List<HouseWork> getHouseWorkByDateAndTeam(Team team, LocalDate fromDate, LocalDate toDate) {
        return houseWorkRepository.findAllByScheduledDateBetweenAndTeam(fromDate, toDate, team);
    }

    public HouseWorkResponseDto getHouseWorkDetail(Long houseWorkId) {
        HouseWork houseWork = getHouseWorkById(houseWorkId);
        List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(houseWorkId).stream().map(MemberDto::from).collect(Collectors.toList());
        return HouseWorkResponseDto.from(houseWork, memberDtoList);
    }

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