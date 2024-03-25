package com.bpwizard.configjdbc.core.exception.handler;


import java.util.Collection;

import com.bpwizard.configjdbc.core.exception.SpringFieldError;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ConstraintViolationExceptionHandler extends AbstractValidationExceptionHandler<ConstraintViolationException> {
    protected static final Logger logger = LoggerFactory.getLogger(ConstraintViolationExceptionHandler.class);
    public ConstraintViolationExceptionHandler() {

        super(ConstraintViolationException.class);
        logger.info("Created");
    }

    @Override
    public Collection<SpringFieldError> getErrors(ConstraintViolationException ex) {
        return SpringFieldError.getErrors(ex.getConstraintViolations());
    }

}
