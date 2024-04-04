package com.bpwizard.configjdbc;

import com.bpwizard.configjdbc.configserver.security.ConfigServerSecurityConfig;
import com.bpwizard.configjdbc.core.security.jwt.JwtAuthorizationProperties;
import com.bpwizard.configjdbc.oauth2client.security.OAuth2ClientSecurityConfig;
import com.bpwizard.configjdbc.resource.security.ResourceServerSecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class SecurityConfig {
    @Autowired
    private JwtAuthorizationProperties props;

    @Bean
    @Order(1)
    @ConditionalOnProperty(prefix = "my.enable", name = "dashboard", havingValue = "true")
    public SecurityFilterChain filterChainResourceServer(HttpSecurity http) throws Exception {
        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = JwtIssuerAuthenticationManagerResolver
                .fromTrustedIssuers("https://login.microsoftonline.com/cddc1229-ac2a-4b97-b78a-0e5cacb5865c/v2.0", "https://frank9999.auth0.com/");

        return  http.securityMatcher("/spring/admin/resource/**")
                .sessionManagement(oauth2 -> oauth2.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf ->csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeRequests(authz -> authz.requestMatchers("/spring/admin/resource/**")// .hasAuthority("SCOPE_Write")
                        .authenticated())
                //.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtCustomizer -> jwtCustomizer.jwkSetUri("https://login.microsoftonline.com/cddc1229-ac2a-4b97-b78a-0e5cacb5865c/v2.0")))
                .addFilterAfter(new CheckOauth2TokenFilter(), ChannelProcessingFilter.class)
                // One issuer
                // .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwkSetUri("https://login.microsoftonline.com/cddc1229-ac2a-4b97-b78a-0e5cacb5865c/discovery/v2.0/keys?appid=cddc1229-ac2a-4b97-b78a-0e5cacb5865c")))
                // multiple issuer
                .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver))
                .build();
    }

    @Bean
    @Order(2)
    @ConditionalOnProperty(prefix = "my.enable", name = "dashboard", havingValue = "true")
    public SecurityFilterChain filterChainOAuth2Client(HttpSecurity http) throws Exception {
        http.securityMatcher("/spring/admin/dashboard", "/spring/admin/dashboard/**", "/spring/admin/api/**", "/spring/admin/auth/**", "/login", "/error", "/actuator/**", "/oauth2/**", "/login/oauth2/**")
                .csrf(csrf -> csrf.ignoringRequestMatchers("/spring/admin/api/csrf")
                            // XSRF-TOKEN cookie won’t be marked HTTP-only, so React can read it and send it back when it tries to manipulate data
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            // https://stackoverflow.com/questions/74447118/csrf-protection-not-working-with-spring-security-6/74521360#74521360
                            // The default behavior of Spring Security 6 is to postpone looking up the CsrfToken until it is required.
                            // add this if it needs the token every time

                            // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa-configuration
                            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                    ). //.ignoringRequestMatchers("/encrypt/**").ignoringRequestMatchers("/decrypt/**")
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
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(ep ->
            ep.oidcUserService(customOidcUserService(props))));
        return http.build();
    }

    @Bean
    @Order(3)
    @ConditionalOnProperty(prefix = "my.enable", name = "dashboard", havingValue = "true")
    public SecurityFilterChain filterChainConfigServer(HttpSecurity http) throws Exception {
        return http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Order(1)
    @ConditionalOnProperty(prefix = "my.enable", name = "dashboard", havingValue = "false")
    public SecurityFilterChain filterChainNoDashboard(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz.requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/spring/admin/**").denyAll()
                .anyRequest().authenticated()).httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public RequestCache refererRequestCache() {
        return new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String referrer = request.getHeader("referer");
                if (referrer == null) {
                    referrer = request.getRequestURL().toString();
                }
                request.getSession().setAttribute("SPRING_SECURITY_SAVED_REQUEST",
                        new SimpleSavedRequest(referrer));

            }
        };
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
