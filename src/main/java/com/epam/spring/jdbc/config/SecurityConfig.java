package com.epam.spring.jdbc.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.epam.spring.jdbc.security.AuthoritiesConstants;
import com.epam.spring.jdbc.security.CustomDaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    DataSource dataSource;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationProvider(customDaoAuthenticationProvider)
            .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers("/")
                .antMatchers("/404");
        
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .httpBasic()
                .realmName("springAdvanced")
            .and()
                .sessionManagement()
            .and()
                .authorizeRequests()
                    .antMatchers("/api/v1/tickets").authenticated()
                    .antMatchers("/booking/**").authenticated()
                    .antMatchers("/user/**", "/booking/event").hasAnyAuthority(AuthoritiesConstants.BOOKING_MANAGER, AuthoritiesConstants.ADMIN)
                    .antMatchers("/upload/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .and()
                .rememberMe()
                .tokenValiditySeconds(1209600)
                .tokenRepository(persistentTokenRepository())
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/booking")
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
                .logout()                                    
                .permitAll()
            .and()
                .exceptionHandling()
                .accessDeniedPage("/denied");
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
    
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

}
