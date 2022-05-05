package com.depromeet.fairer.service.member.oauth.google;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OauthTokenResponse {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String scope;
    private int expiresIn;
    private int refreshTokenExpiresIn;
}
