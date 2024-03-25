package com.bpwizard.configjdbc.core.exception;

public class RelationalProviderException extends RuntimeException {

    public RelationalProviderException(Throwable cause) {
        super(cause);
    }

    public RelationalProviderException(String message) {
        super(message);
    }
}