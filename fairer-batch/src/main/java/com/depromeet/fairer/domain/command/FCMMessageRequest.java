package com.depromeet.fairer.domain.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FCMMessageRequest {
    private Long memberId;
    private String title;
    private String body;

    public static FCMMessageRequest of(Long memberId, String title, String body) {
        return FCMMessageRequest.builder()
                .memberId(memberId)
                .title(title)
                .body(body)
                .build();
    }
}
