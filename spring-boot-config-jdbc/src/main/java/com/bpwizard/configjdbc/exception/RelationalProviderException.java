package com.bpwizard.configjdbc.exception;

public class RelationalProviderException extends RuntimeException {

    public RelationalProviderException(Throwable cause) {
        super(cause);
    }

    public RelationalProviderException(String message) {
        super(message);
    }
}