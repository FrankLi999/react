package com.bpwizard.configjdbc.core.validation;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordForm {

    @NotBlank
    private String code;

    @Password
    private String newPassword;
}
