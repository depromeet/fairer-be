package com.depromeet.fairer.domain.housework.constant;

import com.depromeet.fairer.domain.preset.Preset;
import com.depromeet.fairer.domain.preset.Space;
import com.depromeet.fairer.global.exception.FairerException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import javax.persistence.Enumerated;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RepeatCycle {
    ONCE("O", "당일"),
    DAILY("E", "매일"),
    WEEKLY("W", "1주마다 반복"),
    MONTHLY("M", "1달마다 반복")
    ;

    private String alias;
    private String description;

    public static RepeatCycle of(String alias) {
        return Arrays.stream(values()).filter(R -> R.alias.equals(alias))
                .findFirst().orElseThrow(() -> new FairerException("RepeatCycle's alias is not found"));
    }
}
