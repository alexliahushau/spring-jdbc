package com.epam.spring.jdbc.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epam.spring.jdbc.dao.UserDao;
import com.epam.spring.jdbc.domain.User;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Inject
    private UserDao userDao;

    @Transactional
    public UserDetails loadUserByEmail(final String email, final String password) {

        LOG.debug("Authenticating {}", email);

        final Optional<User> userFromDatabase = findOneByLoginAndPasswordIgnoreCase(email, password);

        return userFromDatabase.map(user -> {
            final List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
                return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found in the database"));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return this.loadUserByEmail(email, StringUtils.EMPTY);
    }

    private Optional<User> findOneByLoginAndPasswordIgnoreCase(final String email, final String password) {

        final Optional<User> user = Optional.ofNullable(userDao.getUserByEmail(email));
        saveUserInSession(user);

        return user;
    }

    private void saveUserInSession(final Optional<User> user) {
        if (user.isPresent()) {

            final AuthenticationManager authenticationManager = new CustomAuthenticationManager();
            final String login = user.get().getEmail();
            final Authentication request = new UsernamePasswordAuthenticationToken(login, login);
            final Authentication result = authenticationManager.authenticate(request);
            final SecurityContext securityContext = SecurityContextHolder.getContext();

            securityContext.setAuthentication(result);
        }
    }

}
