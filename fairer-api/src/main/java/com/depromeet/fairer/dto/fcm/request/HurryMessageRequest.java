package com.depromeet.fairer.dto.fcm.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HurryMessageRequest {

    @NotNull
    private Long houseworkId;

}
