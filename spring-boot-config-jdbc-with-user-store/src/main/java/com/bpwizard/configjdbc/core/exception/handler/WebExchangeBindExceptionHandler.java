package com.bpwizard.configjdbc.core.exception.handler;


import java.util.Collection;

import com.bpwizard.configjdbc.core.exception.SpringFieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebExchangeBindExceptionHandler extends AbstractValidationExceptionHandler<WebExchangeBindException> {
    protected static final Logger logger = LoggerFactory.getLogger(WebExchangeBindExceptionHandler.class);
    public WebExchangeBindExceptionHandler() {

        super(WebExchangeBindException.class);
        logger.info("Created");
    }

    @Override
    public Collection<SpringFieldError> getErrors(WebExchangeBindException ex) {
        return SpringFieldError.getErrors(ex);
    }
}
