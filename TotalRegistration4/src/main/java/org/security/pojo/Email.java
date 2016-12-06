package org.security.pojo;

import javax.validation.constraints.NotNull;
import org.security.validation.EmailConstraint;

/**
 *
 * @author newlife
 */
public class Email {

    @EmailConstraint
    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
