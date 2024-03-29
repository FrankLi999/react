package com.bpwizard.configjdbc.resource.security;

import com.bpwizard.configjdbc.core.security.config.BaseSecurityConfig;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

public class ResourceServerSecurityConfig extends BaseSecurityConfig {
    protected BaseSecurityConfig authorizeRequests(HttpSecurity http) throws Exception {
//        http.authorizeRequests(authz -> authz.antMatchers(Request.HttpMethod.GET, "/foos/**")
//                        .hasAuthority("SCOPE_read")
//                        .antMatchers(Request.HttpMethod.POST, "/foos")
//                        .hasAuthority("SCOPE_write")
//                        .anyRequest()
//                        .authenticated())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
//
        http.authorizeRequests(authz -> authz.requestMatchers("/spring/admin/resource/**")// .hasAuthority("SCOPE_Write")
                .authenticated());
        return this;
    }

    protected BaseSecurityConfig sessionCreationPolicy(HttpSecurity http) throws Exception {
        // No session
        http.sessionManagement(oauth2 -> oauth2.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return this;
    }

    protected BaseSecurityConfig csrf(HttpSecurity http) throws Exception {
        http.csrf(authz -> authz.disable());
        return this;
    }

    protected BaseSecurityConfig cors(HttpSecurity http) throws Exception {
        http.cors(authz -> authz.disable());
        return this;
    }

    protected BaseSecurityConfig login(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        return this;
    }

    protected BaseSecurityConfig oauth2ResourceServer(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return this;
    }
}