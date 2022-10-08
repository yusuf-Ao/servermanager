package io.aycodes.commons.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@RequiredArgsConstructor
@Data
public class NotificationRequest {

    private String              message;
    private String              status;
    private String              timeOfEvent;
    private Long                recipientId;
    private String              ipAddress;

}
