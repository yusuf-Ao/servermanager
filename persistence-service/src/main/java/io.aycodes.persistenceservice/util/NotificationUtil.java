package io.aycodes.persistenceservice.util;

import io.aycodes.amqp.RabbitMQMessageProducer;
import io.aycodes.commons.request.ServerNotificationRequest;
import io.aycodes.commons.request.UserNotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class NotificationUtil {

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void publishNotificationToQueue(final UserNotificationRequest userNotificationRequest) {

        rabbitMQMessageProducer.publish(
                userNotificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
