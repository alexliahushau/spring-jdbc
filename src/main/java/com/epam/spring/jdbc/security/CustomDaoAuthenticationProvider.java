package com.epam.spring.jdbc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Represent implementation of user custom authentication provider.
 */
@Component
public class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(CustomDaoAuthenticationProvider.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserDetailsService customUserDetailsService;

    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken authentication) {

        if (authentication.getCredentials() == null) {
            LOG.error("Authentication failed: no credentials provided");
            throw new InternalAuthenticationServiceException("No credentials provided");
        }

        final String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword()
        )) {
            LOG.error("Authentication failed: password does not match stored value");
            throw new InternalAuthenticationServiceException("Bad credentials");
        }
    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {

        UserDetails loadedUser;

        try {
            loadedUser = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            LOG.error("Authentication failed: user not found");
            throw new InternalAuthenticationServiceException("User not found");
        }
        return loadedUser;
    }
}
