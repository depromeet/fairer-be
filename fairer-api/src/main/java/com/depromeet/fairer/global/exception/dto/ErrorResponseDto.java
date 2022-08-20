package com.depromeet.fairer.global.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {

    private int code;
    private String errorMessage;
    private String referrerUrl;

}
