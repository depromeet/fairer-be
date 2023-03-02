package com.depromeet.fairer.domain.command;

import lombok.Data;

import java.time.LocalTime;

@Data
public class DoHouseworkCommand {
    private Long memberId;
    private String houseworkName;
    private LocalTime scheduledTime;
}
