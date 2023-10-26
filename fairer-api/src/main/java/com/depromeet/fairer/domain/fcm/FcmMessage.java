package com.depromeet.fairer.domain.fcm;

import com.depromeet.fairer.domain.houseworkComplete.HouseworkComplete;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "fcm_message")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_message_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long fcmMessageId;

    private Long memberId;

    private String title;

    private String body;

    public static FcmMessage create(Long memberId, String title, String body) {
        return FcmMessage.builder()
                .memberId(memberId)
                .title(title)
                .body(body)
                .build();
    }
}
