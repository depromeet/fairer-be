package com.depromeet.fairer.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FCMMessageTemplate {
    JOIN_MEMBER_IN_TEAM("%s님이 %s에 참여", "앞으로 함께 평화롭게 집안일 해보아요✨");

    private final String title;
    private final String body;
}
