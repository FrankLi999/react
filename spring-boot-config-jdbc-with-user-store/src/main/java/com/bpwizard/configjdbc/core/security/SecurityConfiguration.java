package com.bpwizard.configjdbc.core.security;

import com.bpwizard.configjdbc.core.audit.IdConverter;
import com.bpwizard.configjdbc.core.audit.SpringWebAuditorAware;
import com.bpwizard.configjdbc.core.exception.handler.*;
import com.bpwizard.configjdbc.core.security.filter.CookieCsrfFilter;
import com.bpwizard.configjdbc.core.security.filter.InternalApiKeyAuthenticationFilter;
import com.bpwizard.configjdbc.core.security.filter.SpaWebFilter;
import com.bpwizard.configjdbc.core.security.jwt.JWTEncryptionService;
import com.bpwizard.configjdbc.core.security.jwt.JWTSignatureService;
import com.bpwizard.configjdbc.core.security.oauth2.JdbcTokenAuthenticationFilter;
import com.bpwizard.configjdbc.core.security.support.GroupsClaimMapper;
import com.bpwizard.configjdbc.core.security.support.NamedOidcUser;
import com.bpwizard.configjdbc.core.security.userstore.entity.User;
import com.bpwizard.configjdbc.core.security.userstore.service.SpringUserDetailsService;
import com.bpwizard.configjdbc.core.security.userstore.entity.Role;
import com.bpwizard.configjdbc.core.exception.ErrorResponseComposer;
import com.bpwizard.configjdbc.core.exception.ExceptionIdMaker;
import com.bpwizard.configjdbc.core.exception.SpringErrorAttributes;
import com.bpwizard.configjdbc.core.exception.SpringExceptionUtils;
import com.bpwizard.configjdbc.core.exception.controller.SpringErrorController;
import com.bpwizard.configjdbc.core.security.jwt.SpringJWTEncryptionService;
import com.bpwizard.configjdbc.core.security.jwt.SpringJWTSignatureService;
import com.bpwizard.configjdbc.core.security.userstore.service.RoleService;
import com.bpwizard.configjdbc.core.security.userstore.service.UserService;
import com.bpwizard.configjdbc.core.service.MailSender;
import com.bpwizard.configjdbc.core.service.MockMailSender;
import com.bpwizard.configjdbc.core.service.SmtpMailSender;
import com.bpwizard.configjdbc.core.service.SpringService;
import com.bpwizard.configjdbc.core.validation.CaptchaValidator;
import com.bpwizard.configjdbc.core.validation.RetypePasswordValidator;
import com.bpwizard.configjdbc.core.validation.UniqueEmailValidator;
import com.bpwizard.configjdbc.core.web.DefaultExceptionHandlerControllerAdvice;
import com.bpwizard.configjdbc.core.web.SpringProperties;
import com.bpwizard.configjdbc.core.web.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.Serializable;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableAsync
@EnableEncryptableProperties
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class SecurityConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Autowired
    private APIAuthenticationErrEntrypoint apiAuthenticationErrEntrypoint;
    @Autowired private SpringUserDetailsService<?, ?> userDetailsService;
    @Autowired
    private JWTSignatureService jwtSignatureService;

    @Autowired private JwtAuthorizationProperties props;
//    @Value("${bpw.api-key}")
//    private String internalApiKey;

    @Autowired private SpringProperties properties;

    /**
     * Configures AuthTokenService if missing
     */
    @Bean
    @ConditionalOnMissingBean(JWTSignatureService.class)
    public JWTSignatureService jwsTokenService(SpringProperties properties) throws JOSEException {

        logger.info("Configuring AuthTokenService");
        return new SpringJWTSignatureService(properties.getJwt().getSecret());
    }


    /**
     * Configures ExternalTokenService if missing
     */
    @Bean
    @ConditionalOnMissingBean(JWTEncryptionService.class)
    public JWTEncryptionService jweTokenService(SpringProperties properties) throws KeyLengthException {

        logger.info("Configuring ExternalTokenService");
        return new SpringJWTEncryptionService(properties.getJwt().getSecret());
    }


    /**
     * Configures Password encoder if missing
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {

        logger.info("Configuring PasswordEncoder");
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     * Configures PermissionEvaluator if missing
     */
    @Bean
    @ConditionalOnMissingBean(PermissionEvaluator.class)
    public PermissionEvaluator permissionEvaluator() {

        logger.info("Configuring SpringPermissionEvaluator");
        return new SpringPermissionEvaluator();
    }

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

    /**
     * Configures a MockMailSender when the property
     * <code>spring.mail.host</code> isn't defined.
     */
    @Bean
    @ConditionalOnMissingBean(MailSender.class)
    @ConditionalOnProperty(name="spring.mail.host", havingValue="foo", matchIfMissing=true)
    public MailSender<?> mockMailSender() {

        logger.info("Configuring MockMailSender");
        return new MockMailSender();
    }


    /**
     * Configures an SmtpMailSender when the property
     * <code>spring.mail.host</code> is defined.
     */
    @Bean
    @ConditionalOnMissingBean(MailSender.class)
    @ConditionalOnProperty("spring.mail.host")
    public MailSender<?> smtpMailSender(JavaMailSender javaMailSender) {

        logger.info("Configuring SmtpMailSender");
        return new SmtpMailSender(javaMailSender);
    }

    @Bean
    public SecurityUtils securityUtils(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        return new SecurityUtils(applicationContext, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(CaptchaValidator.class)
    public CaptchaValidator captchaValidator(SpringProperties properties, RestTemplateBuilder restTemplateBuilder) {

        logger.info("Configuring SpringUserDetailsService");
        return new CaptchaValidator(properties, restTemplateBuilder);
    }
    /**
     * Configures ErrorResponseComposer if missing
     */
    @Bean
    @ConditionalOnMissingBean(ErrorResponseComposer.class)
    public <T extends Throwable>
    ErrorResponseComposer<T> errorResponseComposer(List<AbstractExceptionHandler<T>> handlers) {

        // log.info("Configuring ErrorResponseComposer");
        return new ErrorResponseComposer<T>(handlers);
    }


    /**
     * Configures ExceptionCodeMaker if missing
     */
    @Bean
    @ConditionalOnMissingBean(ExceptionIdMaker.class)
    public ExceptionIdMaker exceptionIdMaker() {

        // log.info("Configuring ExceptionIdMaker");
        return ex -> {

            if (ex == null)
                return null;

            return ex.getClass().getSimpleName();
        };
    }


    /**
     * Configures SpringExceptionUtils
     */
    @Bean
    public SpringExceptionUtils SpringExceptionUtils(MessageSource messageSource,
                                                     LocalValidatorFactoryBean validator,
                                                     ExceptionIdMaker exceptionIdMaker) {

        // log.info("Configuring SpringExceptionUtils");
        return new SpringExceptionUtils(messageSource, validator, exceptionIdMaker);
    }

    @Bean
    @ConditionalOnMissingBean(ConstraintViolationExceptionHandler.class)
    public ConstraintViolationExceptionHandler constraintViolationExceptionHandler() {
        return new ConstraintViolationExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(MultiErrorExceptionHandler.class)
    public MultiErrorExceptionHandler multiErrorExceptionHandler() {
        return new MultiErrorExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(WebExchangeBindExceptionHandler.class)
    public WebExchangeBindExceptionHandler WebExchangeBindExceptionHandler() {
        return new WebExchangeBindExceptionHandler();
    }


    @Bean
    @ConditionalOnMissingBean(BadCredentialsExceptionHandler.class)
    public BadCredentialsExceptionHandler badCredentialsExceptionHandler() {
        return new BadCredentialsExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedExceptionHandler.class)
    public AccessDeniedExceptionHandler accessDeniedExceptionHandler() {
        return new AccessDeniedExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(JsonParseExceptionHandler.class)
    public JsonParseExceptionHandler jsonParseExceptionHandler() {
        return new JsonParseExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(JsonPatchExceptionHandler.class)
    public JsonPatchExceptionHandler jsonPatchExceptionHandler() {
        return new JsonPatchExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(JsonProcessingExceptionHandler.class)
    public JsonProcessingExceptionHandler jsonProcessingExceptionHandler() {
        return new JsonProcessingExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(UsernameNotFoundExceptionHandler.class)
    public UsernameNotFoundExceptionHandler usernameNotFoundExceptionHandler() {
        return new UsernameNotFoundExceptionHandler();
    }

    // web
    /**
     * Configures DefaultExceptionHandlerControllerAdvice if missing
     */
    @Bean
    @ConditionalOnMissingBean(DefaultExceptionHandlerControllerAdvice.class)
    public <T extends Throwable>
    DefaultExceptionHandlerControllerAdvice<T> defaultExceptionHandlerControllerAdvice(
            ErrorResponseComposer<T> errorResponseComposer) {

        logger.info("Configuring DefaultExceptionHandlerControllerAdvice");
        return new DefaultExceptionHandlerControllerAdvice<T>(errorResponseComposer);
    }

    /**
     * Configures an Error Attributes if missing
     */
    @Bean
    @ConditionalOnMissingBean(ErrorAttributes.class)
    public <T extends Throwable>
    ErrorAttributes errorAttributes(ErrorResponseComposer<T> errorResponseComposer) {

        logger.info("Configuring SpringErrorAttributes");
        return new SpringErrorAttributes<>(errorResponseComposer);
    }

    /**
     * Configures an Error Controller if missing
     */
    @Bean
    @ConditionalOnMissingBean(ErrorController.class)
    public ErrorController errorController(ErrorAttributes errorAttributes,
                                           ServerProperties serverProperties,
                                           List<ErrorViewResolver> errorViewResolvers) {

        logger.info("Configuring SpringErrorController");
        return new SpringErrorController(errorAttributes, serverProperties, errorViewResolvers);
    }

    /**
     * Configures an Auditor Aware if missing
     */
    @Bean
    @ConditionalOnMissingBean(AuditorAware.class)
    public <ID extends Serializable>
    AuditorAware<ID> auditorAware() {

        logger.info("Configuring SpringAuditorAware");
        return new SpringWebAuditorAware<>();
    }

    @Bean
    @ConditionalOnProperty(name="bpw.cors.allowed-origins")
    @ConditionalOnMissingBean(CorsConfigurationSource.class)
    public CorsConfigurationSource corsConfigurationSource(SpringProperties properties) {
        //// public SpringCorsConfigurationSource corsConfigurationSource(SpringProperties properties) {

        logger.info("Configuring SpringCorsConfigurationSource");
        CorsConfiguration config = new CorsConfiguration();
        SpringProperties.Cors cors = properties.getCors();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList(cors.getAllowedHeaders()));
        config.setAllowedMethods(Arrays.asList(cors.getAllowedMethods()));
        config.setAllowedOrigins(Arrays.asList(cors.getAllowedOrigins()));
        config.setExposedHeaders(Arrays.asList(cors.getExposedHeaders()));
        config.setMaxAge(cors.getMaxAge());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Configures WebUtils
     */
    @Bean
    public WebUtils webUtils(ApplicationContext applicationContext,
                             ObjectMapper objectMapper) {

        logger.info("Configuring WebUtils");
        return new WebUtils();
    }

    /**
     * Merge ValidationMessages.properties into messages.properties
     */
    @Bean
    @ConditionalOnMissingBean(Validator.class)
    public Validator validator(MessageSource messageSource) {

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }
//    private SpringAuthenticationSuccessHandler authenticationSuccessHandler;
    // service
    @Bean
    @ConditionalOnMissingBean(IdConverter.class)
    public <ID extends Serializable>
    IdConverter<ID> idConverter(SpringService<?,ID> springService) {
        return id -> springService.toId(id);
    }

    /**
     * Configures ServiceUtils
     */
    @Bean
    public ServiceUtils serviceUtils(ApplicationContext applicationContext,
                                     ObjectMapper objectMapper) {

        logger.info("Configuring ServiceUtils");
        return new ServiceUtils();
    }

    /**
     * Configures RetypePasswordValidator if missing
     */
    @Bean
    @ConditionalOnMissingBean(RetypePasswordValidator.class)
    public RetypePasswordValidator retypePasswordValidator() {

        logger.info("Configuring RetypePasswordValidator");
        return new RetypePasswordValidator();
    }

    /**
     * Configures UniqueEmailValidator if missing
     */
    @Bean
    public UniqueEmailValidator uniqueEmailValidator(UserService<?, ?> userService) {

        logger.info("Configuring UniqueEmailValidator");
        return new UniqueEmailValidator(userService);
    }

    @Bean
    public Map<String, Role> preloadedRoles(RoleService<Role, Long> roleService, SpringProperties properties) {

        logger.info("preloadedRoles");   //TODO, load in batch
        Map<String, Role> roles = new HashMap<>();
        String[] roleNames = properties.getAccount().getRolename();
        if (null != roleNames) {
            for (String roleName: roleNames) {
                //Optional<Role> role =
                roleService.findByName(roleName).ifPresent(role -> roles.put(roleName, role));
            }
        }
        return roles;
    }
    // security
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
                .addFilterBefore(new InternalApiKeyAuthenticationFilter(properties.getApiKey()), ChannelProcessingFilter.class)
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
                                                 SpringAuthenticationSuccessHandler authenticationSuccessHandler,
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
               .login(http, authenticationSuccessHandler)
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

    protected SecurityConfiguration login(HttpSecurity http, SpringAuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
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

//    private OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService(JwtAuthorizationProperties props) {
//        final OidcUserService delegate = new OidcUserService();
//        final GroupsClaimMapper mapper = new GroupsClaimMapper(
//                props.getAuthoritiesPrefix(),
//                props.getGroupsClaim(),
//                props.getGroupToAuthorities());
//
//        return userRequest -> {
//            OidcUser oidcUser = delegate.loadUser(userRequest);
//            // Enrich standard authorities with groups
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//            mappedAuthorities.addAll(oidcUser.getAuthorities());
//            mappedAuthorities.addAll(mapper.mapAuthorities(oidcUser));
//
//            oidcUser = new NamedOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo(),oidcUser.getName());
//
//            return oidcUser;
//        };
//    }
}
