package com.depromeet.fairer.domain.command;

import lombok.Data;

@Data
public class InduceAddHouseworkCommand {
    private Long memberId;
    private String teamName;
}
