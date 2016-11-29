package org.security.service;

import org.security.pojo.GenericMessage;
import org.springframework.stereotype.Service;

/**
 *
 * @author newlife
 */
@Service
public interface IMessageReceiverService {

    public void receiveMessage(final GenericMessage message);
}
