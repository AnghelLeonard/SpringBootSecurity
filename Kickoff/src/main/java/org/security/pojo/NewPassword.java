package org.security.pojo;

import org.security.validation.PasswordConstraint;
import org.security.validation.PasswordsConstraint;

/**
 *
 * @author newlife
 */
@PasswordsConstraint
public class NewPassword {

    private String token;

    @PasswordConstraint
    private String reset;

    @PasswordConstraint
    private String password;

    @PasswordConstraint
    private String confirm;

    public NewPassword() {
    }

    public NewPassword(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
