package com.depromeet.fairer.global.config.interceptor;

import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.domain.memberToken.constant.JwtTokenType;
import com.depromeet.fairer.domain.memberToken.constant.RemainingTokenTime;
import com.depromeet.fairer.global.exception.MemberTokenNotFoundException;
import com.depromeet.fairer.repository.memberToken.MemberTokenRepository;
import com.depromeet.fairer.service.member.jwt.TokenProvider;
import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final MemberTokenRepository memberTokenRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행: {}", requestURI);

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNullOrEmpty(authorizationHeader)) {
            sendErrorToResponse(response, HttpStatus.UNAUTHORIZED, "토큰 정보가 없습니다.");
            return false;
        }

        final String token = authorizationHeader.replace("Bearer", "").trim();
        log.info("token: {}", token);

        if (!tokenProvider.validateToken(token)) {
            sendErrorToResponse(response, HttpStatus.FORBIDDEN, "잘못된 토큰 정보입니다.");
            return false;
        }

        Claims tokenClaims = tokenProvider.getTokenClaims(token);

        final String tokenType = tokenClaims.getSubject(); // ACCESS or REFRESH
        final Long memberId = Long.parseLong(tokenClaims.getAudience());

        if (JwtTokenType.ACCESS.name().equals(tokenType)) {
            Date expiration = tokenClaims.getExpiration();

            // access token 만료
            if (tokenProvider.isTokenExpired(new Date(), expiration)) {
                sendErrorToResponse(response, HttpStatus.UNAUTHORIZED, "Access token이 만료되었습니다.");
                return false;
            }

        } else if (JwtTokenType.REFRESH.name().equals(tokenType)) {
            MemberToken memberToken = memberTokenRepository.findByRefreshToken(token)
                    .orElseThrow(() -> new MemberTokenNotFoundException("해당 리프레시 토큰이 존재하지 않습니다."));

            final LocalDateTime refreshTokenExpirationTime = memberToken.getTokenExpirationTime();

            //refresh token 만료 전이면 access token 재발급 및 Authorization Header 세팅
            if (!tokenProvider.isTokenExpired(LocalDateTime.now(), refreshTokenExpirationTime)) {
                Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
                String accessToken = tokenProvider.createAccessToken(memberId, accessTokenExpireTime);
                response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

                memberToken.updateRefreshTokenExpireTime(LocalDateTime.now(), RemainingTokenTime.HOURS_72); // 리프레시 토큰 만료 시간 갱신

            } else if (tokenProvider.isTokenExpired(LocalDateTime.now(), refreshTokenExpirationTime)) { // refresh token 만료 됐을 경우
                sendErrorToResponse(response, HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다.");
                return false;
            }
        }
        return true;
    }

    private void sendErrorToResponse(HttpServletResponse response,HttpStatus httpStatus, String errorMessage) throws IOException {
        log.warn(errorMessage);
        response.sendError(httpStatus.value(), errorMessage);
    }
}
