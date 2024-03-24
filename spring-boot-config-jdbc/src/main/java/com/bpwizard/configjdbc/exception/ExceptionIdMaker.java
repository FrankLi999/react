package com.bpwizard.configjdbc.exception;

@FunctionalInterface
public interface ExceptionIdMaker {

    String make(Throwable t);
}
