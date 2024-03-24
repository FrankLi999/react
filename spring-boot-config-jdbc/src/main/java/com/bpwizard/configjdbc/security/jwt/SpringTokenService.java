package com.bpwizard.configjdbc.security.jwt;


import java.util.Map;

import com.nimbusds.jwt.JWTClaimsSet;

public interface SpringTokenService {

    String MY_IAT = "my-iat";

    String createToken(String aud, String subject, Long expirationMillis, Map<String, Object> claimMap);
    String createToken(String audience, String subject, Long expirationMillis);
    JWTClaimsSet parseToken(String token, String audience);
    JWTClaimsSet parseToken(String token, String audience, long issuedAfter);
    <T> T parseClaim(String token, String claim);
}