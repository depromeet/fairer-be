package com.depromeet.fairer.api;

import com.depromeet.fairer.dto.member.oauth.ClientType;
import com.depromeet.fairer.dto.member.oauth.OauthRequestDto;
import com.depromeet.fairer.dto.member.jwt.ResponseJwtTokenDto;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.service.member.oauth.OauthLoginService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "oauth", description = "oauth API")
@RequestMapping("/api/oauth")
public class OauthLoginController {

    private final OauthLoginService oauthLoginService;

    @Tag(name = "oauth")
    @PostMapping(value = "/login", headers = {"Content-type=application/json"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OAuth 로그인 API", description = "Authorization code로 로그인 시 JWT 토큰 반환, 현재 GOOGLE만 지원")
    public ResponseEntity<ResponseJwtTokenDto> loginOauth(@Valid @RequestBody OauthRequestDto oauthRequestDto, HttpServletRequest httpServletRequest) {
        log.info("=== Oauth login start ===");

        final SocialType socialType = oauthRequestDto.getSocialType();
        ResponseJwtTokenDto jwtTokenDto;

        if (socialType == SocialType.GOOGLE && oauthRequestDto.getClientType() == ClientType.ANDROID) {
            final String accessToken = oauthLoginService.getAccessToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)); // access token 발급
            oauthLoginService.validateLoginParams(socialType, accessToken);
            jwtTokenDto = oauthLoginService.login(socialType, accessToken);

        } else if (socialType == SocialType.GOOGLE && oauthRequestDto.getClientType() == ClientType.IOS) {
            final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (tokenString == null || tokenString.isEmpty()) {
                throw new BadRequestException("토큰이 없습니다.");
            }

            jwtTokenDto = oauthLoginService.googleLoginIos(tokenString);

        } else if (socialType == SocialType.APPLE && oauthRequestDto.getClientType() == ClientType.IOS) {
            final String tokenString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (tokenString == null || tokenString.isEmpty()) {
                throw new BadRequestException("토큰이 없습니다.");
            }

            jwtTokenDto = oauthLoginService.loginAppleIos(tokenString);

        } else {
            throw new BadRequestException("클라이언트 타입이 올바르지 않습니다.");
        }

//        final SocialType socialType = EnumUtils.getEnumIgnoreCase(SocialType.class, socialTypeStr);


        log.info("=== Oauth login end ===");
        return ResponseEntity.ok(jwtTokenDto);
    }

    /**
     * 리프레시 토큰으로만 로그아웃 가능
     *
     * @param refreshToken
     * @return
     */
    @Tag(name = "oauth")
    @PostMapping(value = "/logout")
    @Operation(summary = "로그아웃", description = "refresh token으로만 요청 가능, 로그아웃 처리 시 db에 저장된 refresh token 만료 처리")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization") String refreshToken) {
        oauthLoginService.logout(refreshToken, LocalDateTime.now());
        return ResponseEntity.ok().body("로그아웃이 완료되었습니다.");
    }
}
