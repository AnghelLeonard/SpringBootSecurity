package org.security.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author newlife
 */
@Component
public class AdminDetailsServiceAdapter implements UserDetailsService {

    @Autowired
    private AdminAccountRepository adminAccountRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        AdminAccount account = adminAccountRepository.findByEmail(username);

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return new AdminDetailsAdapter(account);
    }

}
