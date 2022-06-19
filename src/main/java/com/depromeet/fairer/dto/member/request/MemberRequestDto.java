package com.depromeet.fairer.dto.member.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class MemberRequestDto {
    @NotBlank
    private String profilePath;

    @NotBlank
    private String memberName;

    private String statusMessage;
}
