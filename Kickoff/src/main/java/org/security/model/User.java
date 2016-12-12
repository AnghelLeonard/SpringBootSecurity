package org.security.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.security.validation.EmailConstraint;
import org.security.validation.PasswordConstraint;
import org.security.validation.PasswordsConstraint;

/**
 *
 * @author newlife
 */
@Entity
@Table(name = "users")
@PasswordsConstraint
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(length = 50, nullable = false)
    private String username;

    @NotNull
    @Size(min = 10, max = 10, message = "Phone number is not valid.")
    @Column(length = 25, nullable = false)
    private String phone;

    @EmailConstraint
    @NotNull
    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @PasswordConstraint
    @NotNull
    @Column(length = 150, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String roles;

    @Column(nullable = false)
    private boolean enabled;

    @NotNull
    @Transient
    private String confirm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
