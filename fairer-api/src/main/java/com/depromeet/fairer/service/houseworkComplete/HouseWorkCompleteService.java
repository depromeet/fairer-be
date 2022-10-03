package com.depromeet.fairer.service.houseworkComplete;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.houseworkcomplete.HouseWorkCompleteRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

}
