package org.security.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author newlife
 */
@Service
public interface INotificationService {

    public void sendUserPendingRegistrationEmail(final String address);

    public void sendUserResetPasswordEmail(final String address, final String resetpassword);
}
