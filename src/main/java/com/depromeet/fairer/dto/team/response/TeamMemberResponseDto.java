package com.depromeet.fairer.dto.team.response;

import com.depromeet.fairer.domain.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel(value = "팀 멤버 정보 반환 객체")
@Builder
@Data
public class TeamMemberResponseDto {
    @ApiModelProperty(value = "멤버 ID", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "멤버 이름", example = "fairer")
    private String memberName;

    @ApiModelProperty(value = "멤버 프로필이미지 url", example = "http://example.png 또는 '' (공백. 등록되지 않은 경우)")
    private String profilePath;

    public static TeamMemberResponseDto from(Member member) {
        return TeamMemberResponseDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .profilePath(member.getProfilePath())
                .build();
    }

    public static List<TeamMemberResponseDto> toList(Set<Member> members) {
        return members.stream().map(TeamMemberResponseDto::from).collect(Collectors.toList());
    }
}
