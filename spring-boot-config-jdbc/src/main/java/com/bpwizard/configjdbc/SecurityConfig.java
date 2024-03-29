package com.bpwizard.configjdbc;

import com.bpwizard.configjdbc.configserver.security.ConfigServerSecurityConfig;
import com.bpwizard.configjdbc.core.security.jwt.JwtAuthorizationProperties;
import com.bpwizard.configjdbc.oauth2client.security.OAuth2ClientSecurityConfig;
import com.bpwizard.configjdbc.resource.security.ResourceServerSecurityConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;

@Configuration
@EnableWebSecurity
@EnableAsync
@EnableEncryptableProperties
@Slf4j
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class SecurityConfig {
    @Autowired
    private JwtAuthorizationProperties props;

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainResourceServer(HttpSecurity http) throws Exception {
        return new ResourceServerSecurityConfig().filterChainWebApp(http);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainOAuth2Client(HttpSecurity http) throws Exception {
        return new OAuth2ClientSecurityConfig(props).filterChainWebApp(http);
    }

    @Bean
    @Order(3)
    public SecurityFilterChain filterChainConfigServer(HttpSecurity http) throws Exception {
        return new ConfigServerSecurityConfig().filterChainWebApp(http);
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

//    @Bean
//    @ConditionalOnProperty(name="bpw.cors.allowed-origins")
//    @ConditionalOnMissingBean(CorsConfigurationSource.class)
//    public CorsConfigurationSource corsConfigurationSource(SpringProperties properties) {
//        //// public SpringCorsConfigurationSource corsConfigurationSource(SpringProperties properties) {
//
//        logger.info("Configuring SpringCorsConfigurationSource");
//        CorsConfiguration config = new CorsConfiguration();
//        SpringProperties.Cors cors = properties.getCors();
//        config.setAllowCredentials(true);
//        config.setAllowedHeaders(Arrays.asList(cors.getAllowedHeaders()));
//        config.setAllowedMethods(Arrays.asList(cors.getAllowedMethods()));
//        config.setAllowedOrigins(Arrays.asList(cors.getAllowedOrigins()));
//        config.setExposedHeaders(Arrays.asList(cors.getExposedHeaders()));
//        config.setMaxAge(cors.getMaxAge());
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//
}
