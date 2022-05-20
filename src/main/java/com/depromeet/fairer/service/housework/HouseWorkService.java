package com.depromeet.fairer.service.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.housework.request.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.response.HouseWorkDateResponseDto;
import com.depromeet.fairer.dto.housework.response.HouseWorkResponseDto;
import com.depromeet.fairer.dto.housework.response.HouseWorkStatusResponseDto;
import com.depromeet.fairer.dto.housework.response.HouseWorkSuccessCountResponseDto;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.global.exception.MemberTokenNotFoundException;
import com.depromeet.fairer.repository.assignment.AssignmentRepository;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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

    @Transactional
    public List<HouseWorkResponseDto> createHouseWorks(List<HouseWorkRequestDto> houseWorksDto) {
        return houseWorksDto.stream()
                .map(this::createHouseWork)
                .collect(Collectors.toList());
    }

    private HouseWorkResponseDto createHouseWork(HouseWorkRequestDto houseWorkRequestDto) {
        HouseWork houseWork = houseWorkRequestDto.toEntity();
        houseWorkRepository.save(houseWork);

        List<Member> members = memberRepository.findAllById(houseWorkRequestDto.getAssignees());
        for (Member member : members) {
            Assignment assignment = Assignment.builder().houseWork(houseWork).member(member).build();
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

        List<Member> members = memberRepository.findAllById(dto.getAssignees());

        List<Assignment> assignments = assignmentRepository.findAllByHouseWorkAndMemberNotIn(houseWork, members);
        assignmentRepository.deleteAll(assignments);

        for (Member member : members) {
            Optional<Assignment> assignment = assignmentRepository.findByHouseWorkAndMember(houseWork, member);
            if(assignment.isEmpty()) {
                assignmentRepository.save(Assignment.builder().houseWork(houseWork).member(member).build());
            }
        }

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

    /**
     * 날짜별 집안일 조회
     * @param scheduledDate 날짜
     * @return 날짜별 집안일 dto list
     */
    @Transactional
    public HouseWorkDateResponseDto getHouseWork(LocalDate scheduledDate, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberTokenNotFoundException("존재하지 않는 id"));
        List<Assignment> assignmentList = assignmentRepository.findAllByMember(member);
        List<HouseWork> houseWorkList = houseWorkRepository.findAllByScheduledDateAndAssignmentsIn(scheduledDate, assignmentList);

        List<HouseWorkResponseDto> houseWorkResponseDtoList = houseWorkList.stream().map(houseWork -> {
            List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(houseWork.getHouseWorkId()).stream().map(MemberDto::from).collect(Collectors.toList());

            return HouseWorkResponseDto.from(houseWork, memberDtoList);
        }).collect(Collectors.toList());

        long countDone = houseWorkResponseDtoList.stream().filter(HouseWorkResponseDto::getSuccess).count();
        long countLeft = houseWorkResponseDtoList.stream().filter(houseWorkResponseDto -> !houseWorkResponseDto.getSuccess()).count();

        return HouseWorkDateResponseDto.from(scheduledDate, countDone, countLeft, houseWorkResponseDtoList);
    }

    /**
     * 개별 집안일 조회
     * @param houseWorkId 집안일 id
     * @return 집안일 정보 dto
     */
    @Transactional
    public HouseWorkResponseDto getHouseWorkDetail(Long houseWorkId) {
        HouseWork houseWork = getHouseWorkById(houseWorkId);
        List<MemberDto> memberDtoList = memberRepository.getMemberDtoListByHouseWorkId(houseWorkId).stream().map(MemberDto::from).collect(Collectors.toList());
        return HouseWorkResponseDto.from(houseWork, memberDtoList);
    }

    /**
     * 집안일 완료 상태 변경
     * @param houseWorkId 변경할 집안일 id
     * @param toBeStatus 0 or 1
     * @return 변경된 집안일 상태
     */
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