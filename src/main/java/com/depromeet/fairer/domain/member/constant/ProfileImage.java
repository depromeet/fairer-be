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
    PROFILE_1("%2Fblue3.png?alt=media&token=3b0ffa34-cf13-4eb2-9e19-ba91d8952c9a"),
    PROFILE_2("%2Fblue4.png?alt=media&token=d31d6d31-a04e-4c4c-b2f6-8670ca122375"),
    PROFILE_3("%2Forange1.png?alt=media&token=055a8cc0-9f37-4282-9acd-d237563410ae"),
    PROFILE_4("%2Forange2.png?alt=media&token=f5c42e87-708e-4e97-a86f-9688d09a4d89"),
    PROFILE_5("%2Fpink1.png?alt=media&token=8cde7d10-718e-4027-9f6f-02582d907bd5"),
    PROFILE_6("%2Fpink3.png?alt=media&token=984009db-d0a9-4ae8-a89d-cd194b5f0af2"),
    PROFILE_7("%2Fgreen1.png?alt=media&token=8d8711ff-1bfd-42ef-bd0d-64d91dab536c"),
    PROFILE_8("%2Fgreen3.png?alt=media&token=9a326f5b-aaea-491b-aa9a-63b68a19562b"),
    PROFILE_9("%2Fpurple1.png?alt=media&token=f6c2c333-1847-45a7-bd71-5149d41b02a8"),
    PROFILE_10("%2Fpurple3.png?alt=media&token=e7b8a734-75a9-44aa-8c85-a505aee71d00"),
    PROFILE_11("%2Fpurple2.png?alt=media&token=108ec0bc-5d7d-4c44-9f12-6e3b8f6b52a0"),
    PROFILE_12("%2Fyellow1.png?alt=media&token=a4c653b8-0962-4e17-b91b-de3e75af38ba"),
    PROFILE_13("%2Fyellow2.png?alt=media&token=ae0666de-7cf1-41dd-88cc-94c654ff4d44"),
    PROFILE_14("%2Flight_blue1.png?alt=media&token=3b358768-8144-4adb-95fa-822e29ec07c1"),
    PROFILE_15("%2Flight_blue2.png?alt=media&token=f52561f6-818e-4730-b72c-8530c48ccd0f"),
    PROFILE_16("%2Findigo3.png?alt=media&token=933a7607-833f-4f4e-b5b9-e0fba1374d30");

    final String bigImageName;

    public static List<String> getBigImageFullPathList(String domain, String path) {
        return Arrays.stream(values()).map(profileImage -> UriComponentsBuilder.fromHttpUrl(domain).path(path).path(profileImage.bigImageName).build().toString()).collect(Collectors.toList());
    }
}
