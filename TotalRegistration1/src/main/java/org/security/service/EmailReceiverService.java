package org.security.service;

import org.security.pojo.GenericMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author newlife
 */
@Component
public class EmailReceiverService implements IMessageReceiverService {

    @Autowired
    INotificationService notificationService;

    @Override
    @JmsListener(destination = "mailbox", containerFactory = "totalDevFactory")
    public void receiveMessage(GenericMessage message) {
        notificationService.sendUserPendingRegistrationEmail(message.getReceiver());
    }

}
