package com.bpwizard.configjdbc.core.validation;


/**
 * A form using RetypePassword constraint
 * should implement this interface
 */
public interface RetypePasswordForm {

    String getPassword();
    String getRetypePassword();
}
