package com.depromeet.fairer.api;

import com.depromeet.fairer.dto.member.oauth.OauthRequestDto;
import com.depromeet.fairer.dto.member.jwt.ResponseJwtTokenDto;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.service.member.oauth.OauthLoginService;
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
    public ResponseEntity<ResponseJwtTokenDto> loginOauth(@RequestBody OauthRequestDto oauthRequestDto, HttpServletRequest httpServletRequest) {
        log.info("=== Oauth login start ===");

        final String accessToken = oauthLoginService.getAccessToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)); // access token 발급
        final String socialTypeStr = oauthRequestDto.getSocialType();

        oauthLoginService.validateLoginParams(socialTypeStr, accessToken);

        final SocialType socialType = EnumUtils.getEnumIgnoreCase(SocialType.class, socialTypeStr);

        final ResponseJwtTokenDto jwtTokenDto = oauthLoginService.login(socialType, accessToken);
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
