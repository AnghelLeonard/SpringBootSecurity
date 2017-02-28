package org.security.controller;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.security.exceptions.EmailNotFoundException;
import org.security.exceptions.TokenInvalidException;
import org.security.exceptions.UserAlreadyExistException;
import org.security.model.User;
import org.security.pojo.Email;
import org.security.pojo.NewPassword;
import org.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author newlife
 */
@Controller
public class RegistrationController {

    @Autowired
    IUserService userService;

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @GetMapping("/login")
    public String login() {
        return "credentials/login";
    }

    @GetMapping("/register")
    public String registrationShow(final User user) {
        return "/credentials/register";
    }

    @PostMapping("/register")
    public String registrationPerform(HttpServletRequest request,
            @Valid final User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/credentials/register";
        }

        ValidationResult result = recaptchaValidator.validate(request);
        if (!result.isSuccess()) {
            bindingResult.addError(new ObjectError(bindingResult.getObjectName(),
                    "Please check the 'I am not a robot' verification"));
            return "/credentials/register";
        }

        try {
            userService.registerNewUser(user);
        } catch (UserAlreadyExistException e) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(),
                    "email", "This e-mail already exist. Please use another e-mail address."));
            return "/credentials/register";
        }

        return "redirect:/pending";
    }

    @GetMapping("/activate/{token}")
    public String activateUser(final @PathVariable("token") String token) {

        userService.activateNewUser(token);

        return "redirect:/activate";
    }

    @GetMapping("/reset")
    public String resetShow(final Email email) {
        return "/credentials/reset";
    }

    @PostMapping("/reset")
    public String resetPrerequisites(HttpServletRequest request,
            @Valid final Email email, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/credentials/reset";
        }

        try {
            userService.resetPasswordUser(email.getEmail());
        } catch (EmailNotFoundException e) {
            bindingResult.addError(new ObjectError("email",
                    "Sorry, we cannot find your e-mail"));
            return "/credentials/reset";
        }

        return "redirect:/pending";
    }

    @GetMapping("/newpassword/{token}")
    public String newPasswordShow(final @PathVariable("token") String token,
            final @ModelAttribute(name = "newpassword", binding = false) NewPassword newpassword) {

        newpassword.setToken(token);
        return "/credentials/newpassword";
    }

    @PostMapping("/newpassword")
    public String newPasswordPerform(final @Valid @ModelAttribute("newpassword") NewPassword newpassword,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/credentials/newpassword";
        }

        try {
            userService.newPasswordUser(newpassword);
        } catch (TokenInvalidException e) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(),
                    "reset", "Reset password doesn't match the password that we sent to you"));
            return "/credentials/newpassword";
        }

        return "redirect:/activate";
    }
}
