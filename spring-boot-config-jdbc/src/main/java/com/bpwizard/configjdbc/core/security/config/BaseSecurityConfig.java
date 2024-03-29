package com.bpwizard.configjdbc.core.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

public abstract class BaseSecurityConfig {
    public SecurityFilterChain filterChainWebApp(HttpSecurity http) throws Exception {
       this.sessionCreationPolicy(http)
               .logout(http)
               .exceptionHandling(http)
               .tokenAuthentication(http)
               .csrf(http)
               .cors(http)
               .authorizeRequests(http)
               .otherConfiguration(http)
               .login(http)
               .oauth2Client(http)
               .oauth2ResourceServer(http);
        return http.build();
    }

        /**
     * Configuring session creation policy
     */
    protected BaseSecurityConfig sessionCreationPolicy(HttpSecurity http) throws Exception {
        // No session
        // http.sessionManagement(oauth2 -> oauth2.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return this;
    }

    protected BaseSecurityConfig logout(HttpSecurity http) throws Exception {
        // http.logout(authz -> authz.disable()); // we are stateless; so /logout endpoint not needed


//        http.logout(logout -> logout.deleteCookies("JSESSIONID")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/spring/admin/dashboard/integrator/logout")))

        return this;
    }

    protected BaseSecurityConfig exceptionHandling(HttpSecurity http) throws Exception {
        /***********************************************
         * To prevent redirection to the login page
         * when someone tries to access a restricted page
         **********************************************/
        // http.exceptionHandling(authz -> authz.authenticationEntryPoint(new Http403ForbiddenEntryPoint()));
        // new RestAuthenticationEntryPoint();
        return this;
    }

    protected BaseSecurityConfig tokenAuthentication(HttpSecurity http) throws Exception {
        return this;
    }


    protected BaseSecurityConfig csrf(HttpSecurity http) throws Exception {
        return this;
    }

    protected BaseSecurityConfig cors(HttpSecurity http) throws Exception {
        return this;
    }

    protected BaseSecurityConfig authorizeRequests(HttpSecurity http) throws Exception {
        return this;
    }

    protected BaseSecurityConfig otherConfiguration(HttpSecurity http) throws Exception {
        return this;
    }

    protected BaseSecurityConfig login(HttpSecurity http) throws Exception {
        return this;
    }

    protected BaseSecurityConfig oauth2Client(HttpSecurity http) throws Exception {
        return this;
    }
    protected BaseSecurityConfig oauth2ResourceServer(HttpSecurity http) throws Exception {
        return this;
    }

}
