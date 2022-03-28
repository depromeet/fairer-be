package com.depromeet.fairer.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum SocialType {

    GOOGLE, KAKAO;

    @JsonCreator
    public static SocialType from(String type) {
        return SocialType.valueOf(type.toUpperCase());
    }

    public static boolean isSocialType(String type) {
        List<SocialType> collect = Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equals(type))
                .collect(Collectors.toList());
        return collect.size() != 0;
    }
}
