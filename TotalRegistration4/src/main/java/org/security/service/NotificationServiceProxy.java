package org.security.service;

import org.security.pojo.GenericMessage;
import static org.security.util.MessageType.PENDING;
import static org.security.util.MessageType.RESET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author newlife
 */
@Component
public class NotificationServiceProxy implements INotificationServiceProxy {

    @Autowired
    private INotificationService notificationService;

    @Override
    public void sendEmail(GenericMessage genericMessage) {
        switch (genericMessage.getType()) {
            case PENDING:
                notificationService.sendUserPendingRegistrationEmail(genericMessage.getReceiver());
                break;
            case RESET:
                notificationService.sendUserResetPasswordEmail(genericMessage.getReceiver(), genericMessage.getPayload());
                break;
            default:
                throw new RuntimeException("Invalid e-mail type ...");
        }
    }
}
