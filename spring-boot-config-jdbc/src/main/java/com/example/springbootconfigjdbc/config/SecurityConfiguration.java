package com.example.springbootconfigjdbc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().ignoringRequestMatchers("/s2i-integrator/config/encrypt/**").ignoringRequestMatchers("/s2i-integrator/config/decrypt/**").ignoringRequestMatchers("/encrypt/**").ignoringRequestMatchers("/decrypt/**");
//        http.authorizeHttpRequests().requestMatchers("/actuator/**").permitAll().requestMatchers("/dashboard/**").permitAll().requestMatchers("/s2i-integrator/config/actuator/**").permitAll().requestMatchers("/s2i-integrator/config/dashboard/**").permitAll().anyRequest().authenticated();
        http.csrf().ignoringRequestMatchers("/encrypt/**").ignoringRequestMatchers("/decrypt/**");
        http.authorizeHttpRequests().requestMatchers("/actuator/**").permitAll()
                // .requestMatchers("/ui/home/mmm/nnn/ooo/ppp/**").permitAll()
                // .requestMatchers("/admin/console/properties/index.html").permitAll()
                .requestMatchers("/spring/admin/**").permitAll()
                .requestMatchers("/spring/admin/dashboard/index.html").permitAll()
                .requestMatchers("/spring/admin/**").permitAll()
                .anyRequest().authenticated();
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
