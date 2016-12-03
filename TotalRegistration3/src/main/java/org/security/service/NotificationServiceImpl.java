package org.security.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.security.util.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author newlife
 */
@Component
public class NotificationServiceImpl implements INotificationService {

    @Value("${org.security.url.activate}")
    private String activate;

    @Value("${org.security.url.newpassword}")
    private String newpassword;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy-HH:mm");
    private final String expiration = LocalDateTime.now().plusDays(1).format(formatter);

    @Override
    public void sendUserPendingRegistrationEmail(final String address) {

        final String token = CryptUtils.encrypt(address + " " + expiration);

        if (token != null) {
            final Context ctx = new Context();
            ctx.setVariable("name", address);
            ctx.setVariable("expiration", expiration);
            ctx.setVariable("activationlink", activate + token);

            final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            try {
                message.setSubject("Total Registration e-mail");
                message.setFrom("totalregistration@domain.com");
                message.setTo(address);

                final String htmlContent = this.templateEngine.process("mail/html/pending-email", ctx);
                message.setText(htmlContent, true);
            } catch (MessagingException ex) {
                throw new RuntimeException("Unable to build a message for: "
                        + address + "  |  " + ex.getMessage(), ex);
            }

            try {
                this.javaMailSender.send(mimeMessage);
            } catch (MailSendException e) {
                throw new RuntimeException("Cannot send e-mail to address: " + address, e);
            }
        }
    }

    @Override
    public void sendUserResetPasswordEmail(String address, String payload) {

        final String token = CryptUtils.encrypt(address + " "
                + payload + " " + expiration);

        if (token != null) {
            final Context ctx = new Context();
            ctx.setVariable("name", address);
            ctx.setVariable("expiration", expiration);
            ctx.setVariable("resetPassword", payload);
            ctx.setVariable("resetLink", newpassword + token);

            final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            try {
                message.setSubject("Total Registration e-mail");
                message.setFrom("totalregistration@domain.com");
                message.setTo(address);

                final String htmlContent = this.templateEngine.process("mail/html/reset-email", ctx);
                message.setText(htmlContent, true);
            } catch (MessagingException ex) {
                throw new RuntimeException("Unable to build a message for: "
                        + address + "  |  " + ex.getMessage(), ex);
            }

            try {
                this.javaMailSender.send(mimeMessage);
            } catch (MailSendException e) {
                throw new RuntimeException("Cannot send e-mail to address: " + address, e);
            }
        }
    }
}
