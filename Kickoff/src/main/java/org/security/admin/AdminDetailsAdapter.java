package org.security.admin;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author newlife
 */
public class AdminDetailsAdapter implements UserDetails {

    private final AdminAccount account;

    public AdminDetailsAdapter(AdminAccount account) {
        this.account = account;
    }

    public String getFirstName() {
        return account.getFirstName();
    }

    public String getLastName() {
        return account.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_MEMBER", "ROLE_ADMIN");
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
