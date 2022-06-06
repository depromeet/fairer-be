package com.depromeet.fairer.dto.fcm.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FCMMessageRequest {
    @NotNull
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String body;
}
