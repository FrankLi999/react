package com.bpwizard.configjdbc.security;

import com.bpwizard.configjdbc.entity.User;
import com.bpwizard.configjdbc.security.filter.CookieCsrfFilter;
import com.bpwizard.configjdbc.security.filter.InternalApiKeyAuthenticationFilter;
import com.bpwizard.configjdbc.security.filter.SpaWebFilter;
import com.bpwizard.configjdbc.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.security.oauth2.JdbcTokenAuthenticationFilter;
import com.bpwizard.configjdbc.security.support.GroupsClaimMapper;
import com.bpwizard.configjdbc.security.support.NamedOidcUser;
import com.bpwizard.configjdbc.service.SpringService;
import com.bpwizard.configjdbc.web.SpringProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class SecurityConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Autowired
    private APIAuthenticationErrEntrypoint apiAuthenticationErrEntrypoint;
    @Autowired private SpringUserDetailsService<?, ?> userDetailsService;
    @Autowired
    private JWTSignatureService jwtSignatureService;

    @Autowired private JwtAuthorizationProperties props;
    @Value("${my.api-key}")
    private String internalApiKey;

    @Autowired private SpringProperties properties;
    /**
     * Configures AuthenticationSuccessHandler if missing
     */
    @Bean
    @ConditionalOnMissingBean(SpringAuthenticationSuccessHandler.class)
    public SpringAuthenticationSuccessHandler authenticationSuccessHandler(
            ObjectMapper objectMapper, SpringService<?, ?> springService) {

        // logger.info("Configuring AuthenticationSuccessHandler");
        return new SpringAuthenticationSuccessHandler(objectMapper, springService, properties);
    }

    private SpringAuthenticationSuccessHandler authenticationSuccessHandler;
    /**
     * Configures SpringOidcUserService if missing
     */
    @Bean
    @ConditionalOnMissingBean(SpringOidcUserService.class)
    public SpringOidcUserService springOidcUserService(SpringOAuth2UserService<?, ?> springOAuth2UserService) {

        logger.info("Configuring SpringOidcUserService");
        return new SpringOidcUserService(springOAuth2UserService);
    }

    /**
     * Configures SpringOAuth2UserService if missing
     */
    @Bean
    @ConditionalOnMissingBean(SpringOAuth2UserService.class)
    public <U extends User<ID>, ID extends Serializable>
    SpringOAuth2UserService<U,ID> springOAuth2UserService(
            SpringUserDetailsService<U, ?> userDetailsService,
            SpringService<U, ?> springService,
            PasswordEncoder passwordEncoder) {

        logger.info("Configuring SpringOAuth2UserService");
        return new SpringOAuth2UserService<U,ID>(userDetailsService, springService, passwordEncoder);
    }
    /**
     * Configures OAuth2AuthenticationSuccessHandler if missing
     */
    @Bean
    @ConditionalOnMissingBean(OAuth2AuthenticationSuccessHandler.class)
    public OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler(
            SpringProperties properties, JWTSignatureService jwsTokenService) {

        logger.info("Configuring OAuth2AuthenticationSuccessHandler");
        return new OAuth2AuthenticationSuccessHandler<>(properties, jwsTokenService);
    }

    /**
     * Configures OAuth2AuthenticationFailureHandler if missing
     */
    @Bean
    @ConditionalOnMissingBean(OAuth2AuthenticationFailureHandler.class)
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {

        logger.info("Configuring OAuth2AuthenticationFailureHandler");
        return new OAuth2AuthenticationFailureHandler();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainInternalApi(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/spring/admin/internal/api/**")
                .addFilterBefore(new InternalApiKeyAuthenticationFilter(internalApiKey), ChannelProcessingFilter.class)
                .exceptionHandling((auth) -> {
                    auth.authenticationEntryPoint(apiAuthenticationErrEntrypoint);
                })
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainWebApp(HttpSecurity http,
                                                 SpringOidcUserService oidcUserService,
                                                 SpringOAuth2UserService<?, ?> oauth2UserService,
                                                 OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler,
                                                 OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler) throws Exception {
       this.sessionCreationPolicy(http)
               .logout(http)
               .exceptionHandling(http)
               .tokenAuthentication(http)
               .csrf(http)
               .cors(http)
               .authorizeRequests(http)
               .otherConfiguration(http)
               .login(http)
               .oauth2Client(http, oidcUserService, oauth2UserService, oauth2AuthenticationSuccessHandler, oauth2AuthenticationFailureHandler) ;

        return http.build();
    }

    /**
     * Configuring session creation policy
     */
    protected SecurityConfiguration sessionCreationPolicy(HttpSecurity http) throws Exception {
        // No session
        http.sessionManagement(oauth2 -> oauth2.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return this;
    }

    protected SecurityConfiguration logout(HttpSecurity http) throws Exception {
        http.logout(authz -> authz.disable()); // we are stateless; so /logout endpoint not needed


//        http.logout(logout -> logout.deleteCookies("JSESSIONID")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/spring/admin/dashboard/integrator/logout")))

        return this;
    }

    protected SecurityConfiguration exceptionHandling(HttpSecurity http) throws Exception {
        /***********************************************
         * To prevent redirection to the login page
         * when someone tries to access a restricted page
         **********************************************/
        http.exceptionHandling(authz -> authz.authenticationEntryPoint(new Http403ForbiddenEntryPoint()));
        // new RestAuthenticationEntryPoint();
        return this;
    }

    protected SecurityConfiguration tokenAuthentication(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new WebTokenAuthenticationFilter(jwtSignatureService),
//                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JdbcTokenAuthenticationFilter<>(jwtSignatureService, userDetailsService),
                UsernamePasswordAuthenticationFilter.class);
        return this;
    }


    protected SecurityConfiguration csrf(HttpSecurity http) throws Exception {
        // http.csrf().disable();
        // No session
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/spring/admin/api/csrf")
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

    protected SecurityConfiguration cors(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        return this;
    }

    protected SecurityConfiguration authorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz.requestMatchers("/actuator/**").permitAll()
                // .requestMatchers("/ui/home/mmm/nnn/ooo/ppp/**").permitAll()
                // .requestMatchers("/admin/console/properties/index.html").permitAll()
                .requestMatchers("/spring/admin/auth/login", "/error").permitAll()
                .requestMatchers("/spring/admin/dashboard", "/spring/admin/dashboard/index.html", "/spring/admin/dashboard/static/**",
                        "/spring/admin/dashboard/*.ico", "/spring/admin/dashboard/*.json", "/spring/admin/dashboard/*.png").permitAll()
                .requestMatchers("/spring/admin/api/**").permitAll()
                .anyRequest().authenticated());

        return this;
    }

    protected SecurityConfiguration otherConfiguration(HttpSecurity http) throws Exception {

        // Configure a custom Filter to load the CsrfToken on every request, which will return a new cookie if needed.
        http.addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class);
        return this;
    }

    protected SecurityConfiguration login(HttpSecurity http) throws Exception {
        http.formLogin(authz -> authz.loginPage("/spring/admin/dashboard/public/login").permitAll() // properties.getLoginUrl()
                        // .loginProcessingUrl("/spring/admin/dashboard/public/login"))
                /******************************************
                 * Setting a successUrl would redirect the user there. Instead,
                 * let's send 200 and the userDto along with an Authorization token.
                 *****************************************/
                .successHandler(authenticationSuccessHandler)

                /*******************************************
                 * Setting the failureUrl will redirect the user to
                 * that url if login fails. Instead, we need to send
                 * 401. So, let's set failureHandler instead.
                 *******************************************/
                .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
             .httpBasic(Customizer.withDefaults());
        return this;
    }

    protected SecurityConfiguration oauth2Client(HttpSecurity http,
                                                 SpringOidcUserService oidcUserService,
                                                 SpringOAuth2UserService<?, ?> oauth2UserService,
                                                 OAuth2AuthenticationSuccessHandler<?> oauth2AuthenticationSuccessHandler,
                                                 OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler) throws Exception {
//        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(ep ->
//                ep.oidcUserService(customOidcUserService(props))));
        http.oauth2Login(authz -> authz.authorizationEndpoint(a -> a.authorizationRequestRepository(
                new HttpCookieOAuth2AuthorizationRequestRepository(properties)))
                .successHandler(oauth2AuthenticationSuccessHandler)
                .failureHandler(oauth2AuthenticationFailureHandler)
                .userInfoEndpoint(c -> c.oidcUserService(oidcUserService).userService(oauth2UserService))
        );
        return this;
    }


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
