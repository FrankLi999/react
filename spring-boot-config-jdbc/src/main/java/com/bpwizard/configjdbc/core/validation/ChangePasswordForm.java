package com.bpwizard.configjdbc.core.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Change password form.
 */
@RetypePassword
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChangePasswordForm implements RetypePasswordForm {

    @Password
    private String oldPassword;

    @Password
    private String password;

    @Password
    private String retypePassword;
}
