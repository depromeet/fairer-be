package com.depromeet.fairer.domain.command;

import lombok.Data;

@Data
public class FCMMessageResponse {
    private String title;
    private String body;
    private Long memberId;
}
