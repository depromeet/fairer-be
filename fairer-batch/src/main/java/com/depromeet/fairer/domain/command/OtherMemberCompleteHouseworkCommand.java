package com.depromeet.fairer.domain.command;

import lombok.Data;

@Data
public class OtherMemberCompleteHouseworkCommand {
    private Long memberId;
    private String teamMemberName;
    private int count;
}
