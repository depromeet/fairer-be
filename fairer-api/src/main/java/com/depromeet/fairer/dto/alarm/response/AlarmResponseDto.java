package com.depromeet.fairer.dto.alarm.response;

import com.depromeet.fairer.domain.alarm.Alarm;
import com.depromeet.fairer.domain.member.Member;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AlarmResponseDto {
    private Long memberId;
    private Boolean scheduledTimeStatus;
    private Boolean notCompleteStatus;

    public static AlarmResponseDto from(Alarm alarm, Member member) {
        return AlarmResponseDto.builder()
                .memberId(member.getMemberId())
                .notCompleteStatus(alarm.getNotCompleteStatus())
                .scheduledTimeStatus(alarm.getScheduledTimeStatus())
                .build();
    }
}
