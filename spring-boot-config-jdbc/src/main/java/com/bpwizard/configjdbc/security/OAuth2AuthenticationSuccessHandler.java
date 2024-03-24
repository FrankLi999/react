package com.bpwizard.configjdbc.security;


import java.io.Serializable;

import com.bpwizard.configjdbc.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.security.model.UserDto;
import com.bpwizard.configjdbc.web.SpringProperties;
import com.bpwizard.configjdbc.web.WebUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.AllArgsConstructor;

/**
 * Authentication success handler for redirecting the
 * OAuth2 signed in user to a URL with a short lived auth token
 */
@AllArgsConstructor
public class OAuth2AuthenticationSuccessHandler<ID extends Serializable>
        extends SimpleUrlAuthenticationSuccessHandler {

    // private static final Logger log = LogManager.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private SpringProperties properties;
    private JWTSignatureService jwsTokenService;

//	@Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        super.onAuthenticationSuccess(request, response, authentication);
//        this.clearAuthenticationAttributes(request, response);
//    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response) {


        UserDto currentUser = WebUtils.currentUser();

        String shortLivedAuthToken = jwsTokenService.createToken(
                JWTSignatureService.AUTH_AUDIENCE,
                currentUser.getUsername(),
                (long) properties.getJwt().getShortLivedMillis());

        String targetUrl = WebUtils.fetchCookie(request,
                        SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME)
                .map(Cookie::getValue)
                .orElse(properties.getOauth2AuthenticationSuccessUrl());

        WebUtils.deleteCookies(request, response,
                SecurityUtils.AUTHORIZATION_REQUEST_COOKIE_NAME,
                SecurityUtils.BPW_REDIRECT_URI_COOKIE_PARAM_NAME);

        // return targetUrl + shortLivedAuthToken;
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", shortLivedAuthToken)
                .build().toUriString();
    }
}
