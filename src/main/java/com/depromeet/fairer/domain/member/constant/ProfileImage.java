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
    PROFILE_1("%2Fblue3-3x.png?alt=media&token=13eaabdf-1729-46b4-bd23-85c5f8908fdb"),
    PROFILE_2("%2Fblue4-3x.png?alt=media&token=fa208353-fcb9-4996-b596-57e3d07a463d"),
    PROFILE_3("%2Forange1-3x.png?alt=media&token=6a875a5f-3af0-41a7-872c-5c725ef1eb18"),
    PROFILE_4("%2Forange2-3x.png?alt=media&token=bc4f0cc7-76d3-4aa2-95b3-aee112fecb28"),
    PROFILE_5("%2Fpink1-3x.png?alt=media&token=2317c3c0-fdb9-4236-88ed-5991c4b47f17"),
    PROFILE_6("%2Fpink3-3x.png?alt=media&token=bb23c78c-51ec-4fe4-95ad-7a93c7c59f84"),
    PROFILE_7("%2Fgreen1-3x.png?alt=media&token=9a27b20c-602c-4750-a100-659cd08409af"),
    PROFILE_8("%2Fgreen3-3x.png?alt=media&token=a17b07bb-816b-414c-836f-7d53a41d230b"),
    PROFILE_9("%2Fpurple1-3x.png?alt=media&token=828f20f0-6451-40d9-a38f-83e5981ad029"),
    PROFILE_10("%2Fpurple3-3x.png?alt=media&token=b8793e5c-9ebf-474e-805f-e0279e82bfa2"),
    PROFILE_11("%2Fpurple2-3x.png?alt=media&token=1f38ac6e-6c49-49c6-be11-be161f2b3079"),
    PROFILE_12("%2Fyellow1-3x.png?alt=media&token=adbdd7a8-8c57-406f-9433-b120b5626644"),
    PROFILE_13("%2Fyellow2-3x.png?alt=media&token=ccf1bbcf-615f-4950-824a-c51b15ac719a"),
    PROFILE_14("%2Flight-blue1-3x.png?alt=media&token=4df1d835-a7e2-4d04-9a66-3c47237f38ce"),
    PROFILE_15("%2Flight-blue2-3x.png?alt=media&token=5d249bd7-648c-4dba-8d6b-a9b34befd821"),
    PROFILE_16("%2Findigo3-3x.png?alt=media&token=7c0a18bb-d746-4d62-9fa1-87c3876d9eac");

    final String bigImageName;

    public static List<String> getBigImageFullPathList(String domain, String path) {
        return Arrays.stream(values()).map(profileImage -> UriComponentsBuilder.fromHttpUrl(domain).path(path).path(profileImage.bigImageName).build().toString()).collect(Collectors.toList());
    }
}
