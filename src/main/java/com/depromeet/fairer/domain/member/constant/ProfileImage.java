package com.depromeet.fairer.domain.member.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ProfileImage {
    PROFILE_1 ("Profile-1.png", "Profile-2x-1.png"),
    PROFILE_2 ("Profile-2.png", "Profile-2x-2.png"),
    PROFILE_3 ("Profile-3.png", "Profile-2x-3.png"),
    PROFILE_4 ("Profile-4.png", "Profile-2x-4.png"),
    PROFILE_5 ("Profile-5.png", "Profile-2x-5.png"),
    PROFILE_6 ("Profile-6.png", "Profile-2x-6.png"),
    PROFILE_7 ("Profile-7.png", "Profile-2x-7.png"),
    PROFILE_8 ("Profile-8.png", "Profile-2x-8.png"),
    PROFILE_9 ("Profile-9.png", "Profile-2x-9.png"),
    PROFILE_10 ("Profile-10.png", "Profile-2x-10.png"),
    PROFILE_11 ("Profile-11.png", "Profile-2x-11.png"),
    PROFILE_12 ("Profile-12.png", "Profile-2x-12.png"),
    PROFILE_13 ("Profile-13.png", "Profile-2x-13.png"),
    PROFILE_14 ("Profile-14.png", "Profile-2x-14.png"),
    PROFILE_15 ("Profile-15.png", "Profile-2x-15.png"),
    PROFILE_16 ("Profile-16.png", "Profile-2x-16.png");

    final String smallImageName;
    final String bigImageName;

    public static List<String> getSmallImageFullPathList(String domain, String path) {
        return Arrays.stream(values()).map(profileImage -> UriComponentsBuilder.fromHttpUrl(domain).pathSegment(path, profileImage.smallImageName).build().encode().toString()).collect(Collectors.toList());
    }

    public static List<String> getBigImageFullPathList(String domain, String path) {
        return Arrays.stream(values()).map(profileImage -> UriComponentsBuilder.fromHttpUrl(domain).pathSegment(path, profileImage.bigImageName).build().encode().toString()).collect(Collectors.toList());
    }
}
