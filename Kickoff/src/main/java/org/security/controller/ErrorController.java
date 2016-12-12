package org.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author newlife
 */
@ControllerAdvice
public class ErrorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @Value("${org.error.code}")
    private String code;

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {

        LOGGER.error("++++++++++++++++++++++++++++++++++++++++++");
        LOGGER.error("Exception during execution of application:", throwable);
        LOGGER.error("++++++++++++++++++++++++++++++++++++++++++");

        String errorMessage = (throwable != null
                ? (throwable.getMessage().startsWith(code)
                ? throwable.getMessage().substring(code.length() + 1) : "Unknown error. Please, tell us what just happened at org@fashion.com") : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "/error/error";
    }

}
