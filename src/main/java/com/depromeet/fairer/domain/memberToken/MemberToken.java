package com.depromeet.fairer.domain.memberToken;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.memberToken.constant.RemainingTokenTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "member_token")
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberToken {

    @Id
    @Column(name = "member_token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberTokenId;

    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    public static MemberToken create(String refreshToken, LocalDateTime tokenExpiredTime) {
        final MemberToken memberToken = MemberToken.builder()
                .refreshToken(refreshToken)
                .tokenExpirationTime(tokenExpiredTime)
                .build();

        return memberToken;
    }

    /**
     * refresh token이 만료 갱신 기준 이하일 경우 만료 시간 갱신
     * @param now
     * @param remainingTokenTime 해당 시간 이하일 경우 토큰 만료 시간 갱신
     */
    public void updateRefreshTokenExpireTime(LocalDateTime now, RemainingTokenTime remainingTokenTime) {
        final long hours = ChronoUnit.HOURS.between(now, tokenExpirationTime);
        if (hours <= remainingTokenTime.getRemainingTime()) {
            updateTokenExpireTime(now.plusWeeks(2));
        }
    }

    /**
     * 토큰 만료 시간 갱신
     * @param tokenExpirationTime
     */
    public void updateTokenExpireTime(LocalDateTime tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
}
