package com.bpwizard.configjdbc.core.exception;

@FunctionalInterface
public interface ExceptionIdMaker {

    String make(Throwable t);
}
