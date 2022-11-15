package com.depromeet.fairer.domain.housework.constant;

import com.depromeet.fairer.global.exception.FairerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UpdateDeletePolicyType {
    ONLY("O", "단일 일정"),
    HEREAFTER("H", "앞으로 일정"),
    ALL("A", "모든 일정")
    ;

    private String alias;
    private String description;

    public static UpdateDeletePolicyType of(String alias) {
        return Arrays.stream(values()).filter(t -> t.alias.equals(alias.toUpperCase()))
                .findFirst().orElseThrow(() -> new FairerException("Requested RepeatRUDType is not found"));
    }
}
