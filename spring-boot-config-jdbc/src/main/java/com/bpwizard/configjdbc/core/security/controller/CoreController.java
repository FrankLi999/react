package com.bpwizard.configjdbc.core.security.controller;


import com.bpwizard.configjdbc.core.security.dto.JwtResponseDTO;
import com.bpwizard.configjdbc.core.security.dto.RefreshTokenRequestDTO;
import com.bpwizard.configjdbc.core.security.dto.UserProfile;
import com.bpwizard.configjdbc.core.security.SpringPrincipal;
import com.bpwizard.configjdbc.core.security.annotation.CurrentUser;
import com.bpwizard.configjdbc.core.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.core.security.userstore.entity.RefreshToken;
import com.bpwizard.configjdbc.core.security.userstore.service.RefreshTokenService;
import com.bpwizard.configjdbc.core.service.AuthenticationRequest;
import com.bpwizard.configjdbc.core.web.SpringProperties;
import com.bpwizard.configjdbc.core.security.userstore.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping(CoreController.BASE_URI)
@RequiredArgsConstructor
public class CoreController extends SpringController<User<Long>, Long> {
    private static final Logger logger = LoggerFactory.getLogger(CoreController.class);
    public static final String BASE_URI = "/spring/admin/core/api";

    private final SpringProperties properties;
    private final RefreshTokenService refreshTokenService;
    private final JWTSignatureService jwtService;
    private final SpringProperties springProperties;
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            HttpServletRequest request,
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering");
        }
        String shortLivedAuthToken = this.springService.authenticateUser(authenticationRequest);
        User user = this.springService.fetchUserByEmail(authenticationRequest.getEmail());
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting");
        }
        return ResponseEntity.ok(UserProfile.fromUserAndToken(user, shortLivedAuthToken, properties.getJwt().getShortLivedMillis(), sessionId));
    }

    @GetMapping("/time/now")
    public ResponseEntity<Long> now(HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering");
        }

        Long now = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting");
        }
        return ResponseEntity.ok(now);
    }

    @GetMapping(path = "/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userProfile(
            HttpServletRequest request,
            @CurrentUser SpringPrincipal userPrincipal,
            @RequestParam(name="repository", defaultValue = "") String repository,
            @RequestParam(name="workspace", defaultValue = "") String workspace,
            @RequestParam(name="library", defaultValue = "") String library,
            @RequestParam(name="config", defaultValue = "") String siteConfigName) {
        User user = this.springService.fetchUserByEmail(userPrincipal.currentUser().getEmail());

        return ResponseEntity.ok(UserProfile.fromUserAndToken(user));
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.createToken(JWTSignatureService.AUTH_AUDIENCE, userInfo.getEmail(), springProperties.getJwt().getShortLivedMillis());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }
}