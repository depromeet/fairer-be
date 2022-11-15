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
    PROFILE_1("%2Fic_profile1.svg?alt=media&token=13ef5688-3e56-452d-9c63-763958427674"),
    PROFILE_2("%2Fic_profile2.svg?alt=media&token=b2f227e2-ac83-44f3-b143-b04a180f1b89"),
    PROFILE_3("%2Fic_profile3.svg?alt=media&token=fedfeb3c-6fc2-4752-9039-cb6b2534051b"),
    PROFILE_4("%2Fic_profile4.svg?alt=media&token=5123ceb8-510c-4de0-bb2e-d665ec24b73c"),
    PROFILE_5("%2Fic_profile5.svg?alt=media&token=f67556cc-3b5d-4845-b94f-5111263c1028"),
    PROFILE_6("%2Fic_profile6.svg?alt=media&token=109542ce-1d5d-4edb-ac24-72a82160e87a"),
    PROFILE_7("%2Fic_profile7.svg?alt=media&token=88fefe28-e14b-41d1-bc1a-002865e06238"),
    PROFILE_8("%2Fic_profile8.svg?alt=media&token=6cef2c48-444e-42d1-86ae-9328a62799c5"),
    PROFILE_9("%2Fic_profile9.svg?alt=media&token=cbf5c429-2376-49e0-8aee-99ca60efc526"),
    PROFILE_10("%2Fic_profile10.svg?alt=media&token=79107fe2-e40a-4984-9438-a44eafd90b5a"),
    PROFILE_11("%2Fic_profile11.svg?alt=media&token=995e5a46-083c-4210-902b-aabe1ce382d6"),
    PROFILE_12("%2Fic_profile12.svg?alt=media&token=0020c59b-1720-4ff0-8efd-33f77291059a"),
    PROFILE_13("%2Fic_profile13.svg?alt=media&token=208314f3-a68a-4810-b801-dbe67f60b57b"),
    PROFILE_14("%2Fic_profile14.svg?alt=media&token=3770dc20-ac27-46d4-9ea4-125271b35b60"),
    PROFILE_15("%2Fic_profile15.svg?alt=media&token=d4929be9-5882-4bfa-8f1f-58aeffa1888a"),
    PROFILE_16("%2Fic_profile16.svg?alt=media&token=decb7b05-6c49-40f1-af43-13eb69220188");

    final String bigImageName;

    public static List<String> getBigImageFullPathList(String domain, String path) {
        return Arrays.stream(values())
                .map(profileImage -> UriComponentsBuilder.fromHttpUrl(domain)
                        .path(path).path(profileImage.bigImageName)
                        .build().toString())
                .collect(Collectors.toList());
    }
}
