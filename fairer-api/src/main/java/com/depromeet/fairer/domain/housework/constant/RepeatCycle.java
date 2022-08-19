package com.depromeet.fairer.domain.housework.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
public enum RepeatCycle {
    ONCE("당일"),
    WEEKLY("1주마다 반복"),
    MONTHLY("1달마다 반복")
    ;

    private String description;
}
