package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.housework.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.HouseWorkResponseDto;
import com.depromeet.fairer.dto.housework.HouseWorkDateResponseDto;
import com.depromeet.fairer.dto.housework.HouseWorkPresetResponseDto;
import com.depromeet.fairer.dto.housework.HouseWorkStatusResponseDto;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.repository.HouseWorkRepository;
import com.depromeet.fairer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        for (HouseWorkRequestDto houseWorkDto: houseWorksDto) {
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
            }
            houseWorkList.add(houseWork);
        }

        return houseWorkRepository.saveAll(houseWorkList);
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
        List<Long> houseWorkIdList = new ArrayList<>();

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