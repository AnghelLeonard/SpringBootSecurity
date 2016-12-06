package org.security.dao;

import org.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newlife
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT p.password, p.enabled, p.roles FROM User p WHERE p.email = ?1")
    String findUserCredentialsEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User p SET p.password = ?1  WHERE p.email = ?2 AND p.enabled = TRUE")
    int resetUserPassword(String password, String email);

    @Modifying
    @Transactional
    @Query("UPDATE User p SET p.password = ?1  WHERE p.email = ?2 AND p.password = ?3 AND p.enabled = TRUE")
    int newUserPassword(String newpassword, String email, String resetpassword);

    @Modifying
    @Transactional
    @Query("UPDATE User p SET p.enabled = TRUE WHERE p.email = ?1 AND p.enabled = FALSE")
    int enableUserAccount(String email);
}
