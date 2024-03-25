package com.bpwizard.configjdbc.core.exception.handler;


import java.util.Collection;

import com.bpwizard.configjdbc.core.exception.MultiErrorException;
import com.bpwizard.configjdbc.core.exception.SpringFieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class MultiErrorExceptionHandler extends AbstractExceptionHandler<MultiErrorException> {
    protected static final Logger logger = LoggerFactory.getLogger(MultiErrorExceptionHandler.class);

    public MultiErrorExceptionHandler() {
        super(MultiErrorException.class);
        logger.info("Created");
    }

    @Override
    public String getExceptionId(MultiErrorException ex) {

        if (ex.getExceptionId() == null)
            return super.getExceptionId(ex);

        return ex.getExceptionId();
    }

    @Override
    public String getMessage(MultiErrorException ex) {
        return ex.getMessage();
    }

    @Override
    public HttpStatus getStatus(MultiErrorException ex) {
        return ex.getStatus();
    }

    @Override
    public Collection<SpringFieldError> getErrors(MultiErrorException ex) {
        return ex.getErrors();
    }
}
