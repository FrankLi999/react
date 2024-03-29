package com.bpwizard.configjdbc.core.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Version exception, to be thrown when concurrent
 * updates of an entity is noticed.
 */
@ResponseStatus(HttpStatus.CONFLICT) // ResponseStatus doesn't work in reactive project!
public class VersionException extends RuntimeException {

    private static final long serialVersionUID = 6020532846519363456L;

    public VersionException(String entityName, String entityId) {

        super(SpringExceptionUtils.getMessage(
                "com.bpwizard.spring.versionException", entityName, entityId));
    }
}
