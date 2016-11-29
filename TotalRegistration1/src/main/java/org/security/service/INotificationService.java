package org.security.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author newlife
 */
@Service
public interface INotificationService {

    public void sendUserPendingRegistrationEmail(final String address);
}
