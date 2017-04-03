package com.epam.spring.jdbc.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent implementation of user custom authentication manager.
 */
public class CustomAuthenticationManager implements AuthenticationManager {

    private static final List<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(() -> "ROLE_USER");
    }

    public Authentication authenticate(final Authentication authentication) {
        return new UsernamePasswordAuthenticationToken(authentication.getName(),
            "password", AUTHORITIES);
    }
}
