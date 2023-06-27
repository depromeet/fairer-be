package com.depromeet.fairer.dto.member.oauth;

import com.depromeet.fairer.domain.member.constant.SocialType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

@Getter @Setter
@ApiModel(value = "Oauth 로그인 요청 객체", description = "Oauth 로그인을 위한 요청 객체")
public class OauthRequestDto {

    @ApiModelProperty(value = "소셜 로그인 타입 (GOOGLE)")
    private SocialType socialType = SocialType.GOOGLE;

    @ApiModelProperty(value = "로그인 클라이언트 타입")
    private ClientType clientType = ClientType.ANDROID;
}
