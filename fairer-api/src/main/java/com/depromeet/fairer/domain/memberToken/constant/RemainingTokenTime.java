package com.depromeet.fairer.domain.memberToken.constant;

import lombok.Getter;

@Getter
public enum RemainingTokenTime {

    HOURS_72(72);

    RemainingTokenTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    private int remainingTime;
}
