package com.bpwizard.configjdbc.core.security;

import org.springframework.security.core.GrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Our implementation of GrantedAuthority.
 * Simpler than Spring Security's SimpleGrantedAuthority
 * and easily serializable.
 */
@Getter @Setter @AllArgsConstructor
public class SpringGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -1447095521180508501L;

    private String authority;
}
