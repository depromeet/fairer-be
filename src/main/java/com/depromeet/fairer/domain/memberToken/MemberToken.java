package com.depromeet.fairer.domain.memberToken;

import com.depromeet.fairer.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member_token")
@Getter 
@ToString(exclude = "member")
@Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberToken {

    @Id
    @Column(name = "member_token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberTokenId;

    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberToken create(Member member, String refreshToken, LocalDateTime tokenExpiredTime) {
        final MemberToken memberToken = MemberToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .tokenExpirationTime(tokenExpiredTime)
                .build();

        member.updateMemberToken(memberToken);

        return memberToken;
    }
}
