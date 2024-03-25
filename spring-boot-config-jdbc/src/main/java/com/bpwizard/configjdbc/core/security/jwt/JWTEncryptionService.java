package com.bpwizard.configjdbc.core.security.jwt;

public interface JWTEncryptionService extends SpringTokenService {

    String VERIFY_AUDIENCE = "verify";
    String FORGOT_PASSWORD_AUDIENCE = "forgot-password";
    String CHANGE_EMAIL_AUDIENCE = "change-email";
}
