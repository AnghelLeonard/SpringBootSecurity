package org.security.admin;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newlife
 */
@Repository
public class InMemoryAccountRepository implements AdminAccountRepository {

    private final String user;
    private final String password;

    private final List<AdminAccount> accounts;

    public InMemoryAccountRepository(final @Value("${admin.email}") String user, final @Value("${admin.password}") String password) {
        this.user = user;
        this.password = password;

        accounts = new ArrayList<>();
        init();
    }

    private void init() {
        AdminAccount account = new AdminAccount();
        account.setFirstName("-");
        account.setLastName("-");

        account.setEmail(user);
        account.setPassword(password);
        accounts.add(account);
    }

    @Override
    public AdminAccount findByEmail(String email) {
        AdminAccount result = null;
        for (AdminAccount account : accounts) {
            if (account.getEmail().equals(email)) {
                result = account;
                break;
            }
        }
        return result;
    }
}
