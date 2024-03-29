package com.bpwizard.configjdbc.core.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bpwizard.configjdbc.core.security.dto.SpringMailData;
import com.bpwizard.configjdbc.core.security.userstore.entity.SpringUser;
import com.bpwizard.configjdbc.core.exception.SpringExceptionUtils;
import com.bpwizard.configjdbc.core.security.SecurityUtils;
import com.bpwizard.configjdbc.core.security.UserUtils;
import com.bpwizard.configjdbc.core.security.jwt.JWTEncryptionService;
import com.bpwizard.configjdbc.core.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.core.web.SpringProperties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

//import com.bpwizard.spring.boot.commons.SpringProperties.Admin;
//import com.bpwizard.spring.boot.commons.domain.SpringUser;
//import com.bpwizard.spring.boot.commons.exceptions.util.ExceptionUtils;
//import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
//import com.bpwizard.spring.boot.commons.mail.MailSender;
//import com.bpwizard.spring.boot.commons.mail.SpringMailData;
//import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
//import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
//import com.bpwizard.spring.boot.commons.util.SecurityUtils;
//import com.bpwizard.spring.boot.commons.util.UserUtils;

public abstract class AbstractSpringService
        <U extends SpringUser<ID>, ID extends Serializable> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSpringService.class);
    protected PasswordEncoder passwordEncoder;
    protected SpringProperties properties;
    protected JWTSignatureService jwsTokenService;
    protected JWTEncryptionService jweTokenService;
    @SuppressWarnings("rawtypes")
    protected MailSender mailSender;

    /**
     * This method is called after the application is ready.
     * Needs to be public - otherwise Spring screams.
     *
     * @param event
     */
    @EventListener
    public void afterApplicationReady(ApplicationReadyEvent event) {

        logger.info("Starting up Spring boot commons ...");
        onStartup(); // delegate to onStartup()
        logger.info("Spring boot commons started");
    }

    protected abstract void onStartup();

    /**
     * Creates the initial Admin user.
     * Override this if needed.
     */
    protected U createAdminUser() {

        // fetch data about the user to be created
        SpringProperties.Admin initialAdmin = properties.getAccount().getAdmin();

        logger.info("Creating the first admin user: " + initialAdmin.getUsername());

        // create the user
        U user = newUser();
        user.setEmail(initialAdmin.getUsername());
        user.setPassword(passwordEncoder.encode(
                properties.getAccount().getAdmin().getPassword()));
        user.getRoles().add(UserUtils.Role.ADMIN);

        return user;
    }

    protected abstract U newUser();

    protected Map<String, Object> buildContext() {

        // make the context
        Map<String, Object> sharedProperties = new HashMap<String, Object>(2);
        sharedProperties.put("reCaptchaSiteKey", properties.getRecaptcha().getSitekey());
        sharedProperties.put("shared", properties.getShared());

        Map<String, Object> context = new HashMap<>();
        context.put("context", sharedProperties);

        return context;
    }

    protected void initUser(U user) {

        logger.debug("Initializing user: " + user);

        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode the password
        Set<String> removedRoles = new HashSet<>();
        Set<String> newRoles = new HashSet<>();
        makeUnverified(user, removedRoles, newRoles); // make the user unverified
    }

    /**
     * Makes a user unverified
     */
    protected void makeUnverified(U user, Set<String> removedRoles, Set<String> newRoles) {
        newRoles.add(UserUtils.Role.UNVERIFIED);
        user.getRoles().add(UserUtils.Role.UNVERIFIED);
        user.setCredentialsUpdatedMillis(System.currentTimeMillis());
    }

    /**
     * Sends verification mail to a unverified user.
     */
    protected void sendVerificationMail(final U user) {
        try {

            logger.debug("Sending verification mail to: " + user);

            String verificationCode = jweTokenService.createToken(JWTEncryptionService.VERIFY_AUDIENCE,
                    user.getId().toString(), properties.getJwt().getExpirationMillis(),
                    SecurityUtils.mapOf("email", user.getEmail()));

            // make the link
            String verifyLink = properties.getCoreAdminUIUrl()
                    + "/auth/verification/" + user.getId() + "?code=" + verificationCode;

            // send the mail
            sendVerificationMail(user, verifyLink);

            logger.debug("Verification mail to " + user.getEmail() + " queued.");

        } catch (Throwable e) {
            // In case of exception, just log the error and keep silent
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Sends verification mail to a unverified user.
     * Override this method if you're using a different MailData
     */
    @SuppressWarnings("unchecked")
    protected void sendVerificationMail(final U user, String verifyLink) {

        // send the mail
        mailSender.send(SpringMailData.of(user.getEmail(),
                SpringExceptionUtils.getMessage("com.example.spring.verifySubject"),
                SpringExceptionUtils.getMessage(
                        "com.bpwizard.spring.verifyEmail",	verifyLink)));
    }

    /**
     * Mails the forgot password link.
     *
     * @param user
     */
    public void mailForgotPasswordLink(U user) {

        logger.debug("Mailing forgot password link to user: " + user);

        String forgotPasswordCode = jweTokenService.createToken(
                JWTEncryptionService.FORGOT_PASSWORD_AUDIENCE,
                user.getEmail(), properties.getJwt().getExpirationMillis());

        // make the link
        String forgotPasswordLink =	properties.getCoreAdminUIUrl()
                + "/auth/reset-password?code=" + forgotPasswordCode;

        mailForgotPasswordLink(user, forgotPasswordLink);

        logger.debug("Forgot password link mail queued.");
    }


    /**
     * Mails the forgot password link.
     *
     * Override this method if you're using a different MailData
     */
    @SuppressWarnings("unchecked")
    public void mailForgotPasswordLink(U user, String forgotPasswordLink) {

        // send the mail
        mailSender.send(SpringMailData.of(user.getEmail(),
                SpringExceptionUtils.getMessage("com.bpwizard.spring.forgotPasswordSubject"),
                SpringExceptionUtils.getMessage("com.bpwizard.spring.forgotPasswordEmail",
                        forgotPasswordLink)));
    }

    /**
     * Extracts the email id from user attributes received from OAuth2 provider, e.g. Google
     *
     */
    public String getOAuth2Email(String registrationId, Map<String, Object> attributes) {

        return (String) attributes.get(StandardClaimNames.EMAIL);
    }


    /**
     * Extracts additional fields, e.g. name from user attributes received from OAuth2 provider, e.g. Google
     * Override this if you introduce more user fields, e.g. name
     */
    public void fillAdditionalFields(String clientId, U user, Map<String, Object> attributes) {

    }


    /**
     * Checks if the account at the OAuth2 provider is verified
     */
    public boolean getOAuth2AccountVerified(String registrationId, Map<String, Object> attributes) {

        // Facebook no more returns verified
        // https://developers.facebook.com/docs/graph-api/reference/user
        if ("facebook".equals(registrationId))
            return true;

        Object verified = attributes.get(StandardClaimNames.EMAIL_VERIFIED);
        if (verified == null)
            verified = attributes.get("verified");

        try {
            return (boolean) verified;
        } catch (Throwable t) {
            return false;
        }
    }

}