package org.security.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.security.dao.UserRepository;
import org.security.exceptions.UserAlreadyExistException;
import org.security.model.User;
import org.security.pojo.GenericMessage;
import org.security.util.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newlife
 */
@Repository
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IMessageSenderService messageSenderService;

    @Override
    public void registerNewUser(final User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        user.setRoles("ROLE_MEMBER");

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistException("This email exist: " + user.getEmail(), e);
        }

        sendUserPendingRegistrationMessage(user.getEmail());
    }

    @Override
    public void activateNewUser(String token) {

        String decrypted = CryptUtils.decrypt(token);

        if (decrypted != null) {
            final LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = LocalDateTime.
                    parse(decrypted.substring(decrypted.indexOf(" ") + 1),
                            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));

            if (now.isBefore(expiration)) {
                int status = userRepository.enableUserAccount(decrypted.substring(0, decrypted.indexOf(" ")));
                if (status == 1) {
                    return;
                }
            }
        }

        throw new RuntimeException("We cannot activate the account: "
                + (decrypted == null ? "null" : decrypted.substring(0, decrypted.indexOf(" "))));
    }

    private void sendUserPendingRegistrationMessage(String address) {
        messageSenderService.sendMessage("mailbox", new GenericMessage("", address, ""));
    }
}
