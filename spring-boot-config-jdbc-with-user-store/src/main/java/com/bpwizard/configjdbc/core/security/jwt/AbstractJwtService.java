package com.bpwizard.configjdbc.core.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bpwizard.configjdbc.core.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Common JWT Service
 */
public abstract class AbstractJwtService implements SpringTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractJwtService.class);

    protected Payload createPayload(String aud, String subject, Long expirationMillis, Map<String, Object> claimMap) {

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        builder
                //.issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationMillis))
                .audience(aud)
                .subject(subject)
                .claim(MY_IAT, System.currentTimeMillis());

        claimMap.forEach(builder::claim);

        JWTClaimsSet claims = builder.build();

        return new Payload(claims.toJSONObject());
    }


    @Override
    public String createToken(String audience, String subject, Long expirationMillis) {

        return createToken(audience, subject, expirationMillis, new HashMap<>());
    }


    @Override
    public JWTClaimsSet parseToken(String token, String audience) {

        JWTClaimsSet claims = parseToken(token);
        SecurityUtils.ensureCredentials(audience != null &&
                        claims.getAudience().contains(audience),
                "com.bpwizard.spring.wrong.audience");

        long expirationTime = claims.getExpirationTime().getTime();
        long currentTime = System.currentTimeMillis();

        logger.debug("Parsing JWT. Expiration time = " + expirationTime
                + ". Current time = " + currentTime);

        SecurityUtils.ensureCredentials(expirationTime >= currentTime,
                "com.bpwizard.spring.expiredToken");

        return claims;
    }


    @Override
    public JWTClaimsSet parseToken(String token, String audience, long issuedAfter) {

        JWTClaimsSet claims = parseToken(token, audience);

        long issueTime = (long) claims.getClaim(MY_IAT);
        SecurityUtils.ensureCredentials(issueTime >= issuedAfter,
                "com.bpwizard.spring.obsoleteToken");

        return claims;
    }


    @Override
    public <T> T parseClaim(String token, String claim) {

        JWTClaimsSet claims = parseToken(token);
        return (T) claims.getClaim(claim);
    }


    protected abstract JWTClaimsSet parseToken(String token);
}