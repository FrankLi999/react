package com.bpwizard.configjdbc.security;


import java.io.IOException;

import com.bpwizard.configjdbc.web.SpringProperties;
import com.bpwizard.configjdbc.web.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuth2 Authentication failure handler for removing oauth2 related cookies
 */
public class OAuth2AuthenticationFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {

    private SpringProperties properties;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String targetUrl = WebUtils.fetchCookie(request,
                        SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME)
                .map(Cookie::getValue)
                .orElse(properties.getOauth2AuthenticationSuccessUrl());

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        WebUtils.deleteCookies(request, response,
                SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
                SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);

        // super.onAuthenticationFailure(request, response, exception);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

//		WebUtils.deleteCookies(request, response,
//				SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
//				SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);

        // super.onAuthenticationFailure(request, response, exception);
    }
}
