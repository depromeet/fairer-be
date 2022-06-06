package com.depromeet.fairer.dto.fcm;

import com.google.firebase.messaging.Message;
import lombok.Data;

/***
 * https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send
 */
@Data
public class FCMSendRequest {
    private boolean validate_only;
    private Message message;
}

