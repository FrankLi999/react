package com.bpwizard.configjdbc.core.exception.handler;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class JsonProcessingExceptionHandler extends AbstractBadRequestExceptionHandler<JsonProcessingException> {

    public JsonProcessingExceptionHandler() {

        super(JsonProcessingException.class);
        logger.info("Created");
    }
}
