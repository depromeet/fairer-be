package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.housework.HouseWorkRequestDto;
import com.depromeet.fairer.dto.housework.HouseWorkResponseDto;
import com.depromeet.fairer.dto.member.MemberDto;
import com.depromeet.fairer.repository.HouseWorkRepository;
import com.depromeet.fairer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseWorkService {
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<HouseWorkResponseDto> createHouseWorks(List<HouseWorkRequestDto> houseWorksDto) {
        List<HouseWorkResponseDto> houseWorkResponseDtoList = new ArrayList<>();

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
            List<MemberDto> memberDtoList = new ArrayList<>();
            for (Member member : members) {
                Assignment assignment = Assignment.builder().housework(houseWork).member(member).build();
                houseWork.getAssignments().add(assignment);
                memberDtoList.add(MemberDto.from(member));
            }
            houseWorkResponseDtoList.add(HouseWorkResponseDto.from(houseWork, memberDtoList));
            houseWorkRepository.save(houseWork);
        }

        return houseWorkResponseDtoList;
    }
}
