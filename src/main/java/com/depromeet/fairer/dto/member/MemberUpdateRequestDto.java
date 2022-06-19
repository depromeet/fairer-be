package com.depromeet.fairer.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "멤버 정보 업데이트 요청 객체", description = "멤버 정보 업데이트 요청 객체")
public class MemberUpdateRequestDto {

    @ApiModelProperty(value = "멤버 이름")
    @NotNull
    private String memberName;

    @ApiModelProperty(value = "프로필 url")
    @NotNull
    private String profileUrl;
}
