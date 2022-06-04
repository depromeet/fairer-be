package com.depromeet.fairer.dto.fcm.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FCMMessageResponse {
    private String title;
    private String body;
    private Long memberId;

    public static FCMMessageResponse of(String title, String body, Long memberId) {
        return FCMMessageResponse.builder()
                .title(title)
                .body(body)
                .memberId(memberId)
                .build();
    }
}
