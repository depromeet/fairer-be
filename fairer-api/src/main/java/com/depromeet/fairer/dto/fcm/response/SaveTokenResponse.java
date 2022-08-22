package com.depromeet.fairer.dto.fcm.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveTokenResponse {
    private String token;

    public static SaveTokenResponse of(String token) {
        return SaveTokenResponse.builder()
                .token(token)
                .build();
    }
}
