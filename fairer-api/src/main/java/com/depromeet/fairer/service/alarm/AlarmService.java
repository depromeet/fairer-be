package com.depromeet.fairer.service.alarm;


import com.depromeet.fairer.domain.alarm.Alarm;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.alarm.request.AlarmRequestDto;
import com.depromeet.fairer.dto.alarm.response.AlarmResponseDto;
import com.depromeet.fairer.global.exception.FairerException;
import com.depromeet.fairer.repository.alarm.AlarmRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberService memberService;

    public AlarmResponseDto getAlarmStatus(Long memberId) {
        Member member = memberService.find(memberId);
        Alarm alarm = alarmRepository.findByMember(member);
        return AlarmResponseDto.from(alarm, member);
    }

    public AlarmResponseDto updateAlarmStatus(Long memberId, AlarmRequestDto request) {
        Member member = memberService.find(memberId);

        Alarm alarm = alarmRepository.findByMember(member);
        alarm.setNotCompleteStatus(request.getNotCompleteStatus());
        alarm.setScheduledTimeStatus(request.getScheduledTimeStatus());
        alarmRepository.save(alarm);

        return AlarmResponseDto.from(alarm, member);
    }
}
