package com.depromeet.fairer.service.member.jwt;

import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.dto.member.jwt.TokenDto;
import com.depromeet.fairer.domain.memberToken.constant.JwtTokenType;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.repository.member.MemberRepository;
import com.depromeet.fairer.repository.memberToken.MemberTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final MemberTokenRepository memberTokenRepository;
    private final MemberRepository memberRepository;

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    private static final String BEARER_TYPE = "bearer";

    public static Long getMemberId(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey("fairer-backend") //jwt 만들 때 사용했던 키. static 메서드 사용하기 위해서 String으로 하드 코딩.
                    .parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getAudience());
        } catch (Exception e) {
            log.warn("토큰 변환 중 에러 발생: {}", token);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 엑세스/리프레시 토큰 dto 생성
     * refresh token 저장 로직 포함
     * @param memberId
     * @return
     */
    public TokenDto createTokenDto(Long memberId) {

        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(memberId, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);

        final MemberToken memberToken = MemberToken.create(refreshToken, DateTimeUtils.convertToLocalDateTime(refreshTokenExpireTime));
        memberTokenRepository.save(memberToken);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    private String createRefreshToken(Long memberId, Date expirationTime) {
        return Jwts.builder()
                .setSubject(JwtTokenType.REFRESH.name())
                .setAudience(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String createAccessToken(Long memberId, Date expirationTime) {
        return Jwts.builder()
                .setSubject(JwtTokenType.ACCESS.name())
                .setAudience(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    /**
     * access token 만료 시간 생성
     * @return
     */
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    /**
     * refresh token 만료 시간 생성
     * @return
     */
    private Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    public boolean isTokenExpired(Date now, Date tokenExpiredTime) {
        if (now.after(tokenExpiredTime)) { // 토큰 만료된 경우
            return true;
        }
        return false;
    }

    public boolean isTokenExpired(LocalDateTime now, LocalDateTime tokenExpiredTime) {
        if (now.isAfter(tokenExpiredTime)) { // 토큰 만료된 경우
            return true;
        }
        return false;
    }

    /**
     * 토큰 유효 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {  // 토큰 변조
            log.warn("토큰 변조 감지: {}", token);
            e.printStackTrace();
        } catch (Exception e) {
            log.warn("토큰 검증 에러 발생: {}", token);
            e.printStackTrace();
            throw e;
        }

        return false;
    }

    /**
     * 토큰 파싱 후 property 변환
     * @param token
     * @return
     */
    public Claims getTokenClaims(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw e;
        }
        return claims;
    }

}
