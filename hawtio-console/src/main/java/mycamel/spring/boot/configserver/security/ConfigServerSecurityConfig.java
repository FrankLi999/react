package mycamel.spring.boot.configserver.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import mycamel.spring.boot.core.security.config.BaseSecurityConfig;

public class ConfigServerSecurityConfig extends BaseSecurityConfig {

	protected BaseSecurityConfig authorizeRequests(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz -> authz.anyRequest().authenticated());
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

}
