package com.bpwizard.configjdbc.security.jwt;

public interface JWTSignatureService extends SpringTokenService {

    String USER_CLAIM = "user";
    String AUTH_AUDIENCE = "auth";
}
