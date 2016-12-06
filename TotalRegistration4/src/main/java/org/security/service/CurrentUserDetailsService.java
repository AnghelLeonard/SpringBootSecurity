package org.security.service;

import org.security.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newlife
 */
@Repository
public class CurrentUserDetailsService implements UserDetailsService {

    public CurrentUserDetailsService() {
        super();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        final String user = findUserCredentialsEmail(email);

        if (user == null || user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with email=%s was not found ...", email));
        }

        final String[] credentials = user.split(",");
        return new org.springframework.security.core.userdetails.User(email, credentials[0],
                Boolean.valueOf(credentials[1]), true, true, true, AuthorityUtils.createAuthorityList(credentials[2]));
    }

    @Transactional(readOnly = true)
    private String findUserCredentialsEmail(final String email) {
        return userRepository.findUserCredentialsEmail(email);
    }
}
