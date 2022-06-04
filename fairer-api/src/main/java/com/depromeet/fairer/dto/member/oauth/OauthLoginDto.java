package com.depromeet.fairer.dto.member.oauth;

import com.depromeet.fairer.domain.member.constant.SocialType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthLoginDto {

    private String accessToken;

    private SocialType socialType;
}
