package com.depromeet.fairer.dto.fcm;

import com.google.firebase.messaging.Message;
import lombok.Builder;
import lombok.Data;

/***
 * https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send
 */
@Data
@Builder
public class FCMSendRequest {
    private boolean validate_only;
    private Message message;

    public static FCMSendRequest of(Message message, boolean validate_only) {
        return FCMSendRequest.builder()
                .message(message)
                .validate_only(validate_only)
                .build();
    }
}

