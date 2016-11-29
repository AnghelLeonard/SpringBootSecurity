package org.security.controller;

import javax.validation.Valid;
import org.security.model.User;
import org.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/register")
    public String registrationShow(final User user) {
        return "/credentials/register";
    }

    @PostMapping("/register")
    public String registrationPerform(@Valid final User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
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
