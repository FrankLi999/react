package com.bpwizard.configjdbc.security.filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Configure a custom Filter to load the CsrfToken on every request, which will return a new cookie if needed.
 *
 * Spring Security 6 doesn't set a XSRF-TOKEN cookie by default.
 * This solution is
 * <a href="https://github.com/spring-projects/spring-security/issues/12141#issuecomment-1321345077">
 * recommended by Spring Security.</a>
 */
public class CookieCsrfFilter extends OncePerRequestFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa-configuration
//        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
//        // Render the token value to a cookie by causing the deferred token to be loaded
//        csrfToken.getToken();
        filterChain.doFilter(request, response);
    }
}
