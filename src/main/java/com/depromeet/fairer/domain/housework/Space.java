package com.depromeet.fairer.domain.housework;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
public enum Space {

    ENTRANCE, LIVINGROOM, BATHROOM, OUTSIDE, ROOM, KITCHEN;

    @JsonIgnore
    public static Space from(String space) {
        return Space.valueOf(space.toUpperCase());
    }

    public static boolean isSupportSpace(String space) {
        final long count = Arrays.stream(Space.values())
                .filter(s -> s.name().equals(space))
                .count();
        return count != 0;
    }
}
