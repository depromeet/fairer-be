package com.depromeet.fairer.service;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.housework.HouseWorkRequestDto;
import com.depromeet.fairer.repository.HouseWorkRepository;
import com.depromeet.fairer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HouseWorkService {
    private final ModelMapper modelMapper;
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
        }).orElseThrow(() -> new NoSuchElementException("존재하지 않는 집안일 입니다."));
    }

    public void deleteHouseWork(Long id) {
        try{
            houseWorkRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 집안일 입니다.");
        }
    }
}
