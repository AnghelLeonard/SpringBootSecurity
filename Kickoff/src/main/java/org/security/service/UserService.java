package org.security.service;

import org.security.dao.UserRepository;
import org.security.exceptions.EmailNotFoundException;
import org.security.exceptions.TokenInvalidException;
import org.security.exceptions.UserAlreadyExistException;
import org.security.model.User;
import org.security.pojo.GenericMessage;
import org.security.pojo.NewPassword;
import org.security.util.CryptUtils;
import org.security.util.MessageType;
import org.security.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newlife
 */
@Repository
public class UserService implements IUserService {

    @Value("${org.password.extralength}")
    private byte pel;

    @Value("${org.error.code}")
    private String code;

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
    public void activateNewUser(final String token) {

        final String decrypted = CryptUtils.decrypt(token);

        if (decrypted != null) {
            final String then = decrypted.substring(decrypted.indexOf(" ") + 1);

            if (Utils.checkThenAgainstNow(then)) {
                final int status = userRepository.
                        enableUserAccount(decrypted.substring(0, decrypted.indexOf(" ")));
                if (status == 1) {
                    return;
                }
            } else {
                sendUserPendingRegistrationMessage(decrypted.substring(0, decrypted.indexOf(" ")));
                throw new RuntimeException(code + ":This activation link has expired. We have sent you a new link. Please use it in the next 24h.");
            }
        }

        throw new RuntimeException(code + ":We cannot activate the account: "
                + (decrypted == null ? "null" : decrypted.substring(0, decrypted.indexOf(" "))));
    }

    @Override
    public void resetPasswordUser(final String address) {

        final String resetpassword = Utils.randomPassword(pel);
        final int status = userRepository.resetUserPassword(resetpassword, address);

        if (status == 1) {
            sendUserResetPasswordMessage(address, resetpassword);
            return;
        }

        throw new EmailNotFoundException();
    }

    @Override
    public void newPasswordUser(final NewPassword newpassword) {

        final String decrypted = CryptUtils.decrypt(newpassword.getToken());

        if (decrypted != null) {
            final String resetpassword = decrypted.substring(decrypted.indexOf(" ") + 1, decrypted.lastIndexOf(" "));
            if (resetpassword.equals(newpassword.getReset())) {
                final String then = decrypted.substring(decrypted.lastIndexOf(" ") + 1);
                if (Utils.checkThenAgainstNow(then)) {
                    final int status = userRepository.
                            newUserPassword(passwordEncoder.encode(newpassword.getPassword()),
                                    decrypted.substring(0, decrypted.indexOf(" ")), newpassword.getReset());
                    if (status == 1) {
                        return;
                    }
                } else {
                    sendUserResetPasswordMessage(decrypted.substring(0, decrypted.indexOf(" ")), resetpassword);
                    throw new RuntimeException(code + ":This password reset link has expired. We have sent you a new link. Please use it in the next 24h.");
                }
            } else {
                throw new TokenInvalidException();
            }
        }

        throw new RuntimeException(code + ":We cannot reset password for account: "
                + (decrypted == null ? "null" : decrypted.substring(0, decrypted.indexOf(" "))));
    }

    private void sendUserPendingRegistrationMessage(final String address) {
        messageSenderService.sendMessage("mailbox", new GenericMessage(address, MessageType.PENDING));
    }

    private void sendUserResetPasswordMessage(final String address, final String resetpassword) {
        messageSenderService.sendMessage("mailbox", new GenericMessage(address, resetpassword, MessageType.RESET));
    }
}
