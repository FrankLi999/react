package com.bpwizard.configjdbc.security.filter;

import java.io.IOException;

import com.bpwizard.configjdbc.exception.SpringExceptionUtils;
import com.bpwizard.configjdbc.security.SecurityUtils;
import com.bpwizard.configjdbc.security.SpringPrincipal;
import com.bpwizard.configjdbc.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.security.model.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebTokenAuthenticationFilter extends OncePerRequestFilter {

    private JWTSignatureService jwsTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("Inside SpringTokenAuthenticationFilter ...");

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(SecurityUtils.TOKEN_PREFIX)) { // token present

            logger.debug("Found a token");
            String token = header.substring(7);

            try {

                Authentication auth = createAuthFromToken(token);
                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.debug("Token authentication successful");

            } catch (Exception e) {

                logger.debug("Token authentication failed - " + e.getMessage());

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Authentication Failed: " + e.getMessage());

                return;
            }

        } else

            logger.debug("Token authentication skipped");

        filterChain.doFilter(request, response);
    }

    protected Authentication createAuthFromToken(String token) {

        JWTClaimsSet claims = jwsTokenService.parseToken(token, JWTSignatureService.AUTH_AUDIENCE);
        UserDto userDto = SecurityUtils.getUserDto(claims);
        if (userDto == null)
            userDto = fetchUserDto(claims);

        SpringPrincipal principal = new SpringPrincipal(userDto);

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    /**
     * Default behaviour is to throw error. To be overridden in auth service.
     *
     * @param username
     * @return
     */
    protected UserDto fetchUserDto(JWTClaimsSet claims) {
        throw new AuthenticationCredentialsNotFoundException(
                SpringExceptionUtils.getMessage("com.example.spring.userClaimAbsent"));
    }
}
