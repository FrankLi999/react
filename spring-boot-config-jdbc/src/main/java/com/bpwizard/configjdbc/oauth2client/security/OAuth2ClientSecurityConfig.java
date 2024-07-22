package com.bpwizard.configjdbc.oauth2client.security;

import com.bpwizard.configjdbc.core.security.config.BaseSecurityConfig;

import com.bpwizard.configjdbc.core.security.filter.CookieCsrfFilter;
import com.bpwizard.configjdbc.core.security.filter.SpaWebFilter;
import com.bpwizard.configjdbc.core.security.jwt.JwtAuthorizationProperties;
import com.bpwizard.configjdbc.core.security.support.GroupsClaimMapper;
import com.bpwizard.configjdbc.core.security.support.NamedOidcUser;
import lombok.RequiredArgsConstructor;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import com.bpwizard.configjdbc.core.security.csrf.SpaCsrfTokenRequestHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class OAuth2ClientSecurityConfig extends BaseSecurityConfig {
    private final JwtAuthorizationProperties props;
    protected BaseSecurityConfig securityMatcher(HttpSecurity http) throws Exception {
        // No session
        http.securityMatcher("/spring/admin/dashboard", "/spring/admin/dashboard/**", "/spring/admin/api/**", "/spring/admin/auth/**", "/error", "/actuator/**");
        return this;
    }
    protected BaseSecurityConfig authorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz.requestMatchers("/actuator/**").permitAll()
                // .requestMatchers("/ui/home/mmm/nnn/ooo/ppp/**").permitAll()
                // .requestMatchers("/admin/console/properties/index.html").permitAll()
                .requestMatchers("/spring/admin/auth/login", "/error").permitAll()
                .requestMatchers("/spring/admin/dashboard", "/spring/admin/dashboard/index.html", "/spring/admin/dashboard/static/**",
                        "/spring/admin/dashboard/*.ico", "/spring/admin/dashboard/*.json", "/spring/admin/dashboard/*.png").permitAll()
                .requestMatchers("/spring/admin/api/**").authenticated());
        return this;
    }
    protected BaseSecurityConfig csrf(HttpSecurity http) throws Exception {
//        // http.csrf().disable();
//        // No session
        http.csrf(csrf -> csrf.requireCsrfProtectionMatcher( new AntPathRequestMatcher("/spring/admin/api/**")).ignoringRequestMatchers("/spring/admin/api/csrf")
                // XSRF-TOKEN cookie won’t be marked HTTP-only, so React can read it and send it back when it tries to manipulate data
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // https://stackoverflow.com/questions/74447118/csrf-protection-not-working-with-spring-security-6/74521360#74521360
                // The default behavior of Spring Security 6 is to postpone looking up the CsrfToken until it is required.
                // add this if it needs the token every time

                // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa-configuration
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        );
        return this;
    }

    protected BaseSecurityConfig cors(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        return this;
    }

    protected BaseSecurityConfig oauth2Client(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(ep ->
                ep.oidcUserService(customOidcUserService(props))));
        return this;
    }

    protected BaseSecurityConfig otherConfiguration(HttpSecurity http) throws Exception {
        http.addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class);
        return this;
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService(JwtAuthorizationProperties props) {
        final OidcUserService delegate = new OidcUserService();
        final GroupsClaimMapper mapper = new GroupsClaimMapper(
                props.getAuthoritiesPrefix(),
                props.getGroupsClaim(),
                props.getGroupToAuthorities());

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            // Enrich standard authorities with groups
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            mappedAuthorities.addAll(oidcUser.getAuthorities());
            mappedAuthorities.addAll(mapper.mapAuthorities(oidcUser));

            oidcUser = new NamedOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo(),oidcUser.getName());

            return oidcUser;
        };
    }
}