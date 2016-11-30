package org.security.controller;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.security.model.User;
import org.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;

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

    @GetMapping("/register")
    public String registrationShow(final User user) {
        return "/credentials/register";
    }

    @PostMapping("/register")
    public String registrationPerform(HttpServletRequest request, @Valid final User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/credentials/register";
        }

        ValidationResult result = recaptchaValidator.validate(request);
        if (!result.isSuccess()) {
            bindingResult.addError(new ObjectError("user", "Please check the 'I am not a robot' verification"));
            return "/credentials/register";
        }

        userService.registerNewUser(user);

        return "redirect:/pending?email=" + HtmlUtils.htmlEscape(user.getEmail());
    }

    @GetMapping("/activate/{token}")
    public String activateUser(final @PathVariable("token") String token) {

        userService.activateNewUser(token);

        return "/credentials/activate";
    }
}
