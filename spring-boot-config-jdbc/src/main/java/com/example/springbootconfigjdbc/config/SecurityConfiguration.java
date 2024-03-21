package com.example.springbootconfigjdbc.config;

import com.example.springbootconfigjdbc.config.support.GroupsClaimMapper;
import com.example.springbootconfigjdbc.config.support.NamedOidcUser;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import com.example.springbootconfigjdbc.filter.*;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class SecurityConfiguration {
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthorizationProperties props) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/spring/admin/api/encrypt/*", "/spring/admin/api/decrypt/*", "/spring/admin/api/csrf")
                    // XSRF-TOKEN cookie won’t be marked HTTP-only, so React can read it and send it back when it tries to manipulate data
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // https://stackoverflow.com/questions/74447118/csrf-protection-not-working-with-spring-security-6/74521360#74521360
                    // The default behavior of Spring Security 6 is to postpone looking up the CsrfToken until it is required.
                    // add this if it needs the token every time

                    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa-configuration
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                ). 
          authorizeHttpRequests((authz -> authz.requestMatchers("/actuator/**").permitAll()
                    // .requestMatchers("/ui/home/mmm/nnn/ooo/ppp/**").permitAll()
                    // .requestMatchers("/admin/console/properties/index.html").permitAll()
                    .requestMatchers("/spring/admin/dashboard", "/spring/admin/dashboard/index.html", "/spring/admin/dashboard/static/**",
                            "/spring/admin/dashboard/*.ico", "/spring/admin/dashboard/*.json", "/spring/admin/dashboard/*.png").permitAll()
                    .requestMatchers("/spring/admin/api/**").permitAll()
                    .anyRequest().authenticated()))
                 // Configure a custom Filter to load the CsrfToken on every request, which will return a new cookie if needed.
                 .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                 .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class);
     
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(ep ->
                        ep.oidcUserService(customOidcUserService(props))));
        return http.build();
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
