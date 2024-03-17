package com.example.springbootconfigjdbc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import com.example.springbootconfigjdbc.filter.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().ignoringRequestMatchers("/s2i-integrator/config/encrypt/**").ignoringRequestMatchers("/s2i-integrator/config/decrypt/**").ignoringRequestMatchers("/encrypt/**").ignoringRequestMatchers("/decrypt/**");
//        http.authorizeHttpRequests().requestMatchers("/actuator/**").permitAll().requestMatchers("/dashboard/**").permitAll().requestMatchers("/s2i-integrator/config/actuator/**").permitAll().requestMatchers("/s2i-integrator/config/dashboard/**").permitAll().anyRequest().authenticated();
        http.csrf().ignoringRequestMatchers("/encrypt/**").ignoringRequestMatchers("/decrypt/**");
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
     
        // http.authorizeHttpRequests().requestMatchers("/actuator/**").permitAll()
        //         // .requestMatchers("/ui/home/mmm/nnn/ooo/ppp/**").permitAll()
        //         // .requestMatchers("/admin/console/properties/index.html").permitAll()
        //         .requestMatchers("/spring/admin/**").permitAll()
        //         .requestMatchers("/spring/admin/dashboard/index.html").permitAll()
        //         .requestMatchers("/spring/admin/**").permitAll()
        //         .anyRequest().authenticated();
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
