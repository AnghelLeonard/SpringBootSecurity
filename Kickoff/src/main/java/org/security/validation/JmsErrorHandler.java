package org.security.validation;

import org.security.controller.ErrorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

/**
 *
 * @author newlife
 */
@Service
public class JmsErrorHandler implements ErrorHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @Override
    public void handleError(Throwable t) {
        LOGGER.error("++++++++++++++++++++++++++++++++++++++++++");
        LOGGER.error("Error in JMS listener", t);
        LOGGER.error("++++++++++++++++++++++++++++++++++++++++++");
    }
}
