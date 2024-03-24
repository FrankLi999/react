package com.bpwizard.configjdbc.security;


import java.io.Serializable;
import java.util.Optional;

import com.bpwizard.configjdbc.entity.User;
import com.bpwizard.configjdbc.exception.SpringExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * UserDetailsService, as required by Spring Security.
 */
public class SpringUserDetailsService
        <U extends User<ID>, ID extends Serializable>
        implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SpringUserDetailsService.class);

    private final UserService<U,ID> userService;

    public SpringUserDetailsService(UserService<U, ID> userService) {

        this.userService = userService;
        logger.info("Created");
    }

    @Override
    public SpringPrincipal loadUserByUsername(String username)
            throws UsernameNotFoundException {

        logger.debug("Loading user having username: " + username);

        // delegates to findUserByUsername
        U user = findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        SpringExceptionUtils.getMessage("com.bpwizard.spring.userNotFound", username)));

        logger.debug("Loaded user having username: " + username);

        return new SpringPrincipal(user.toUserDto());
    }

    /**
     * Finds a user by the given username. Override this
     * if you aren't using email as the username.
     */
    public Optional<U> findUserByUsername(String username) {
        return userService.findByEmail(username);
    }

    /**
     * Finds a user by the given username. Override this
     * if you aren't using email as the username.
     */
    public Optional<U> findUserByEmail(String email) {
        return userService.findByEmail(email);
    }
}
