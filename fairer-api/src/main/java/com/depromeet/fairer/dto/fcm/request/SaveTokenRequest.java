package com.depromeet.fairer.dto.fcm.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveTokenRequest {
    @NotBlank
    private String token;
}
