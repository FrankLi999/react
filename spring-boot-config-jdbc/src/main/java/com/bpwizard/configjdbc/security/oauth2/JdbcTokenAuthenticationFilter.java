package com.bpwizard.configjdbc.security.oauth2;


import java.io.Serializable;

import com.bpwizard.configjdbc.entity.User;
import com.bpwizard.configjdbc.security.ServiceUtils;
import com.bpwizard.configjdbc.security.SpringUserDetailsService;
import com.bpwizard.configjdbc.security.filter.WebTokenAuthenticationFilter;
import com.bpwizard.configjdbc.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.security.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nimbusds.jwt.JWTClaimsSet;

public class JdbcTokenAuthenticationFilter<U extends User<ID>, ID extends Serializable>
        extends WebTokenAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTokenAuthenticationFilter.class);

    private SpringUserDetailsService<U, ID> userDetailsService;

    public JdbcTokenAuthenticationFilter(JWTSignatureService jwsTokenService,
                                               SpringUserDetailsService<U, ID> userDetailsService) {

        super(jwsTokenService);
        this.userDetailsService = userDetailsService;

        logger.info("Created");
    }

    @Override
    protected UserDto fetchUserDto(JWTClaimsSet claims) {

        String username = claims.getSubject();
        U user = userDetailsService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        logger.debug("User found ...");

        ServiceUtils.ensureCredentialsUpToDate(claims, user);
        UserDto userDto = user.toUserDto();
        userDto.setPassword(null);

        return userDto;
    }
}
