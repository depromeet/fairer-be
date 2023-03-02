package com.depromeet.fairer.dto.alarm.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRequestDto {
    @NotNull
    private Boolean scheduledTimeStatus;
    @NotNull
    private Boolean notCompleteStatus;
}
