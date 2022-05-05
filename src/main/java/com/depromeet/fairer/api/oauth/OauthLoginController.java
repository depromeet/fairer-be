package com.depromeet.fairer.api.oauth;

import com.depromeet.fairer.dto.member.oauth.OauthLoginDto;
import com.depromeet.fairer.dto.member.oauth.OauthRequestDto;
import com.depromeet.fairer.dto.member.jwt.ResponseJwtTokenDto;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.service.member.oauth.OauthLoginService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OauthLoginController {

    private final OauthLoginService oauthLoginService;

    @PostMapping(value = "/oauth/login", headers = {"Content-type=application/json"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OAuth 로그인 API", description = "Authorization code로 로그인 시 JWT 토큰 반환, 현재 GOOGLE만 지원")
    @ApiImplicitParams({
            @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, defaultValue = "authorization code", dataType = "String", value = "authorization code", required = true, paramType = "header")
    })
    public ResponseEntity<ResponseJwtTokenDto> loginOauth(@RequestBody OauthRequestDto oauthRequestDto, HttpServletRequest httpServletRequest) {
        log.info("=== Oauth login start ===");

        final String accessToken = oauthLoginService.getAccessToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)); // access token 발급
        final String socialTypeStr = oauthRequestDto.getSocialType();

        validateLoginParams(socialTypeStr, accessToken);

        final SocialType socialType = EnumUtils.getEnumIgnoreCase(SocialType.class, socialTypeStr);

        final ResponseJwtTokenDto jwtTokenDto = login(socialType, accessToken);
        log.info("=== Oauth login end ===");
        return ResponseEntity.ok(jwtTokenDto);
    }

    public ResponseJwtTokenDto login(SocialType socialType, String accessToken) {

        final OauthLoginDto oauthLoginDto = OauthLoginDto.builder().accessToken(accessToken).socialType(socialType).build();
        return oauthLoginService.createMemberAndJwt(oauthLoginDto);
    }

    private void validateLoginParams(String socialType, String accessToken) {
        validateSocialType(socialType);
        validateAccessToken(accessToken);
    }

    private void validateSocialType(String socialType) {
        if (!EnumUtils.isValidEnumIgnoreCase(SocialType.class, socialType)) {
            throw new InvalidParameterException("잘못된 소셜 타입입니다. 'GOOGLE' 중에 입력해주세요.");
        }
    }

    private void validateAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new InvalidParameterException("Access 토큰값을 입력해주세요.");
        }
    }
}
