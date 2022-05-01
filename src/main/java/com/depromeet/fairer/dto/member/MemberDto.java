package com.depromeet.fairer.dto.member;

import lombok.Data;

@Data
public class MemberDto {
    private Long memberId;
    private String memberName;
    private String profilePath;

    public MemberDto() {
    }
}