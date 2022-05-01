package com.depromeet.fairer.service.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.housework.HouseWorkRequestDto;
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
}
