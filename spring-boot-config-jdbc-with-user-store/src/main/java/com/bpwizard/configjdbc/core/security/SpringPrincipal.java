package com.bpwizard.configjdbc.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.bpwizard.configjdbc.core.security.model.UserDto;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Spring Security Principal, implementing both OidcUser, UserDetails
 */
@Getter @Setter @RequiredArgsConstructor
public class SpringPrincipal implements OidcUser, UserDetails, CredentialsContainer {

    private static final long serialVersionUID = -7849730155307434535L;

    @Getter(AccessLevel.NONE)
    private final UserDto userDto;

    private Map<String, Object> attributes;
    private String name;
    private Map<String, Object> claims;
    private OidcUserInfo userInfo;
    private OidcIdToken idToken;

    public UserDto currentUser() {
        return userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<String> roles = userDto.getRoles();

        Collection<SpringGrantedAuthority> authorities = roles.stream()
                .map(role -> new SpringGrantedAuthority("ROLE_" + role))
                // .map(role -> new SpringGrantedAuthority(role))
                .collect(Collectors.toCollection(() ->
                        new ArrayList<>(roles.size() + 2)));

        if (userDto.isGoodUser()) {

            authorities.add(new SpringGrantedAuthority("ROLE_" + SecurityUtils.GOOD_USER));
            // authorities.add(new SpringGrantedAuthority(SecurityUtils.GOOD_USER));

            if (userDto.isGoodAdmin())
                authorities.add(new SpringGrantedAuthority("ROLE_" + SecurityUtils.GOOD_ADMIN));
            // authorities.add(new SpringGrantedAuthority(SecurityUtils.GOOD_ADMIN));
        }

        return authorities;
    }

    // UserDetails ...

    @Override
    public String getPassword() {

        return userDto.getPassword();
    }

    @Override
    public String getUsername() {

        return userDto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    @Override
    public void eraseCredentials() {

        userDto.setPassword(null);
        attributes = null;
        claims = null;
        userInfo = null;
        idToken = null;
    }
}
