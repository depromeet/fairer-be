package com.depromeet.fairer.dto.member.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@ApiModel(value="멤버 정보 업데이트 요청 객체", description = "멤버 정보 업데이트 요청 객체")
@AllArgsConstructor @NoArgsConstructor
public class MemberUpdateRequestDto {
    @NotBlank
    @ApiModelProperty(value = "프로필 이미지 경로")
    private String profilePath;

    @NotBlank
    @ApiModelProperty(value = "멤버 이름")
    private String memberName;

    @ApiModelProperty(value = "상태 메세지")
    private String statusMessage;
}
