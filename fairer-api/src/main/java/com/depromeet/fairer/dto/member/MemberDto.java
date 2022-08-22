package com.depromeet.fairer.dto.member;

import com.depromeet.fairer.domain.member.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @ApiModelProperty(value = "멤버 ID", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "멤버 이름", example = "fairer")
    private String memberName;

    @ApiModelProperty(value = "멤버 프로필이미지 url", example = "http://example.png 또는 '' (공백. 등록되지 않은 경우)")
    private String profilePath;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .profilePath(member.getProfilePath())
                .build();
    }

    public static List<MemberDto> toList(Set<Member> members) {
        return members.stream().map(MemberDto::from).collect(Collectors.toList());
    }
}

