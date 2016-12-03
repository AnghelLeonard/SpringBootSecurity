package org.security.service;

import org.security.dao.UserRepository;
import org.security.exceptions.UserAlreadyExistException;
import org.security.model.User;
import org.security.pojo.GenericMessage;
import org.security.pojo.NewPassword;
import org.security.util.CryptUtils;
import org.security.util.MessageType;
import org.security.util.Utils;
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
            final String then = decrypted.substring(decrypted.indexOf(" ") + 1);

            if (Utils.checkThenAgainstNow(then)) {
                final int status = userRepository.enableUserAccount(decrypted.substring(0, decrypted.indexOf(" ")));
                if (status == 1) {
                    return;
                }
            }
        }

        throw new RuntimeException("We cannot activate the account: "
                + (decrypted == null ? "null" : decrypted.substring(0, decrypted.indexOf(" "))));
    }

    @Override
    public int resetPasswordUser(String address) {

        final String resetpassword = Utils.randomPassword(2);        
        final int status = userRepository.resetUserPassword(resetpassword, address);

        if (status == 1) {
            sendUserResetPasswordMessage(address, resetpassword);
        }

        return status;
    }

    @Override
    public void newPasswordUser(NewPassword newpassword) {

        String decrypted = CryptUtils.decrypt(newpassword.getToken());

        if (decrypted != null) {
            final String then = decrypted.substring(decrypted.lastIndexOf(" ") + 1);
            if (Utils.checkThenAgainstNow(then)) {
                final String resetpassword = decrypted.substring(decrypted.indexOf(" ") + 1, decrypted.lastIndexOf(" "));
                if (resetpassword.equals(newpassword.getReset())) {                                             
                    final int status = userRepository.
                            newUserPassword(passwordEncoder.encode(newpassword.getPassword()),
                                    newpassword.getReset(),
                                    decrypted.substring(0, decrypted.indexOf(" ")));
                    if (status == 1) {
                        return;
                    }
                }
            }
        }

        throw new RuntimeException("We cannot reset password for account: "
                + (decrypted == null ? "null" : decrypted.substring(0, decrypted.indexOf(" "))));
    }

    private void sendUserPendingRegistrationMessage(String address) {
        messageSenderService.sendMessage("mailbox", new GenericMessage(address, MessageType.PENDING));
    }

    private void sendUserResetPasswordMessage(String address, final String resetpassword) {
        messageSenderService.sendMessage("mailbox", new GenericMessage(address, resetpassword, MessageType.RESET));
    }
}
