package com.depromeet.fairer.dto.member;

import com.depromeet.fairer.domain.member.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @ApiModelProperty(value = "멤버 ID", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "멤버 닉네임", example = "fairer")
    private String memberName;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .build();
    }
}

