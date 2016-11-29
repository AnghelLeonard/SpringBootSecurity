package org.security.service;

import org.security.pojo.GenericMessage;
import org.springframework.stereotype.Service;

/**
 *
 * @author newlife
 */
@Service
public interface IMessageSenderService {

    public void sendMessage(final String destination, final GenericMessage message);
}
