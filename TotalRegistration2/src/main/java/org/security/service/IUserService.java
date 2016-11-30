package org.security.service;

import org.security.model.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author newlife
 */
@Service
public interface IUserService {

    public void registerNewUser(final User user);

    public void activateNewUser(final String token);
}
