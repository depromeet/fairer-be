package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.housework.*;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.repository.HouseWorkRepository;
import com.depromeet.fairer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseWorkService {
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Iterable<HouseWork> createHouseWorks(List<HouseWorkRequestDto> houseWorksDto) {
        List<HouseWork> houseWorkList = new ArrayList<>();
        for (HouseWorkRequestDto houseWorkDto : houseWorksDto) {
            HouseWork houseWork = HouseWork.builder()
                    .space(houseWorkDto.getSpace())
                    .houseWorkName(houseWorkDto.getHouseWorkName())
                    .scheduledDate(houseWorkDto.getScheduledDate())
                    .scheduledTime(houseWorkDto.getScheduledTime())
                    .success(false)
                    .successDateTime(null)
                    .build();

            Iterable<Member> members = memberRepository.findAllById(houseWorkDto.getAssignees());
            for (Member member : members) {
                Assignment assignment = Assignment.builder().housework(houseWork).member(member).build();
                houseWork.getAssignments().add(assignment);
                member.getAssignments().add(assignment);
            }
            houseWorkList.add(houseWork);
        }

        return houseWorkRepository.saveAll(houseWorkList);
    }

    public HouseWork updateHouseWork(Long id, HouseWorkRequestDto dto) {
        return houseWorkRepository.findById(id).map(houseWork -> {
            houseWork.setSpace(dto.getSpace());
            houseWork.setHouseWorkName(dto.getHouseWorkName());
            houseWork.setScheduledDate(dto.getScheduledDate());
            houseWork.setScheduledTime(dto.getScheduledTime());

//            TODO: 변경된 assignee에 대해서만 assignment 할당되도록 수정 필요
            houseWork.getAssignments().clear();
            Iterable<Member> members = memberRepository.findAllById(dto.getAssignees());
            for (Member member : members) {
                Assignment assignment = Assignment.builder()
                        .housework(houseWork)
                        .member(member)
                        .build();
                houseWork.getAssignments().add(assignment);
                member.getAssignments().add(assignment);
            }

            return houseWorkRepository.save(houseWork);
        }).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 집안일 입니다."));
    }

    public void deleteHouseWork(Long id) {
        try{
            houseWorkRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 집안일 입니다.");
        }
    }

    public HouseWorkSuccessCountResponseDto getSuccessCount(String scheduledDate) {
        LocalDate startDate = LocalDate.parse(scheduledDate).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate endDate = LocalDate.parse(scheduledDate).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusWeeks(1);
        Long count = houseWorkRepository.getHouseWorkSuccessCount(3L, startDate, endDate);
        return HouseWorkSuccessCountResponseDto.of(count);
    }

    /**
     * 날짜별 집안일 조회
     * @param scheduledDate 날짜
     * @return 날짜별 집안일 dto list
     */
    @Transactional
    public HouseWorkDateResponseDto getHouseWork(LocalDate scheduledDate){
        // 같은 날짜의 집안일 정보 리스트
        List<HouseWorkResponseDto> houseWorkResponseDtos = new ArrayList<>();
        List<Long> houseWorkIdList;

        // houseWorkId찾기
        houseWorkIdList = houseWorkRepository.findHouseWorkIdByDate(scheduledDate);

        houseWorkIdList.forEach(houseWorkId -> houseWorkResponseDtos.add(
                getHouseWorkDetail(houseWorkId)
        ));
        // houseWorkResponseDtos 완성

        int countDone = houseWorkRepository.countDone(scheduledDate);
        int countLeft = houseWorkRepository.countLeft(scheduledDate);

        return HouseWorkDateResponseDto.from(scheduledDate, countDone, countLeft, houseWorkResponseDtos);
    }

    /**
     * 개별 집안일 조회
     * @param houseWorkId 집안일 id
     * @return 집안일 정보 dto
     */
    @Transactional
    public HouseWorkResponseDto getHouseWorkDetail(Long houseWorkId) {
        HouseWork houseWork = houseWorkRepository.findById(houseWorkId)
                .orElseThrow(() -> new IllegalArgumentException("housework id가 존재하지 않습니다."));

        List<MemberDto> memberDtolist = houseWorkRepository.addMemberDtoById(houseWorkId);

        return HouseWorkResponseDto.from(houseWork, memberDtolist);
    }

    /**
     * 집안일 완료 상태 변경
     * @param houseWorkId 변경할 집안일 id
     * @return 변경된 집안일 상태
     */
    @Transactional
    public HouseWorkStatusResponseDto updateHouseWorkStatus(Long houseWorkId,
                                                            String toBeStatus) {
        boolean status;
        log.info(toBeStatus);
        if (toBeStatus.equals("끝냈어요")) {
            houseWorkRepository.updateStatusTrue(houseWorkId);
            status = true;
        } else {
            houseWorkRepository.updateStatusFalse(houseWorkId);
            status = false;
        }
        return new HouseWorkStatusResponseDto(houseWorkId, status);
    }

    /**
     * 공간 -> 집안일 프리셋 조회
     * @param space 공간
     * @return 집안일 이름 list
     */
    @Transactional
    public HouseWorkPresetResponseDto getHouseWorkPreset(String space){
        List<String> houseWorks;
        houseWorks = houseWorkRepository.getHouseWorkPreset(space);
        return new HouseWorkPresetResponseDto(space, houseWorks);
    }
}