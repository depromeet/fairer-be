package com.depromeet.fairer.domain.member.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum SocialType {
    GOOGLE, KAKAO;
}
