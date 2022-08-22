package com.depromeet.fairer.domain.command;

import lombok.Data;

@Data
public class NotCompleteHouseworkRemindCommand {
    private Long memberId;
    private Long totalCount;
    private String houseworkName;
}
