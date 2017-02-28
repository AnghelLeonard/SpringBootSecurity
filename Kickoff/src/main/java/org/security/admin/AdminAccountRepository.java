package org.security.admin;

/**
 *
 * @author newlife
 */
public interface AdminAccountRepository {

    AdminAccount findByEmail(final String email);
}
