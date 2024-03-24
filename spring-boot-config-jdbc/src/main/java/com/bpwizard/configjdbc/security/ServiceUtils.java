package com.bpwizard.configjdbc.security;


import java.io.Serializable;

import com.bpwizard.configjdbc.security.jwt.SpringTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.nimbusds.jwt.JWTClaimsSet;
import com.bpwizard.configjdbc.entity.User;
/**
 * Useful helper methods
 */
public class ServiceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServiceUtils.class);

    public ServiceUtils() {

        logger.info("Created");
    }


    /**
     * Signs a user in
     *
     * @param user
     */
    public static <U extends User<ID>, ID extends Serializable>
    void login(U user) {

        SpringPrincipal principal = new SpringPrincipal(user.toUserDto());

        Authentication authentication = // make the authentication object
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication); // put that in the security context
        principal.eraseCredentials();
    }


    /**
     * Throws BadCredentialsException if
     * user's credentials were updated after the JWT was issued
     */
    public static <U extends User<ID>, ID extends Serializable>
    void ensureCredentialsUpToDate(JWTClaimsSet claims, U user) {

        long issueTime = (long) claims.getClaim(SpringTokenService.MY_IAT);

        SecurityUtils.ensureCredentials(issueTime >= user.getCredentialsUpdatedMillis(),
                "com.bpwizard.spring.obsoleteToken");
    }
}
