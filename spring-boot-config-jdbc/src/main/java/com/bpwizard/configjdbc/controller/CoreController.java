package com.bpwizard.configjdbc.controller;


import com.bpwizard.configjdbc.dto.UserProfile;
import com.bpwizard.configjdbc.security.SpringPrincipal;
import com.bpwizard.configjdbc.security.annotation.CurrentUser;
import com.bpwizard.configjdbc.service.AuthenticationRequest;
import com.bpwizard.configjdbc.web.SpringProperties;
import com.bpwizard.configjdbc.entity.User;
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
}