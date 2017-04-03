package com.epam.spring.jdbc.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.mapper.UserRowMapper;

/**
 * @author Aliaksandr_Liahushau
 */
@Component
public class UserDao {
    final static String INSERT_USER = "INSERT INTO USERS (first_name, last_name, email, birthday, roles, password) VALUES (?,?,?,?,?,?)";
    final static String INSERT_USER_SYSTEM_MESSAGE = "INSERT INTO USER_SYSTEM_MESSAGE (USER_ID, MESSAGE, TIME) VALUES (?,?,?)";
    final static String UPDATE_USER = "UPDATE USERS SET first_name = ?, last_name = ?, email = ?, birthday = ?, roles = ?, password = ? WHERE USER_ID = ?";
    
    final static String DELETE_USER = "DELETE FROM USERS WHERE USER_ID = ?";
        
    final static String GET_ALL_USERS = "SELECT * FROM USERS";
    final static String GET_USER_BY_ID = GET_ALL_USERS + " WHERE USER_ID = ?";
    final static String GET_USER_BY_EMAIL = GET_ALL_USERS + " WHERE EMAIL = ?";

    @Inject
    private JdbcTemplate jdbcTemplate;
    
    @Inject
    private PasswordEncoder passwordEncoder;
    
    public User save(User user) {
        final String first_name = user.getFirstName();
        final String last_name = user.getLastName();
        final String email = user.getEmail();
        final Date birthday = user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null;
        final String roles = user.getRoles().toString();
        final String password = passwordEncoder.encode(user.getPassword());
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Long userId = user.getId();
        
        try {
            if (userId == null) {
                jdbcTemplate.update((PreparedStatementCreator) conn -> {
                    final PreparedStatement ps = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, first_name);
                    ps.setString(2, last_name);
                    ps.setString(3, email);
                    ps.setDate(4, birthday);
                    ps.setString(5, roles);
                    ps.setString(6, password);

                    return ps;
                } , keyHolder);
                user.setId(keyHolder.getKey().longValue());
            } else {
                jdbcTemplate.update(UPDATE_USER, first_name, last_name, email, birthday, roles, password, userId);
            }
            
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
            user = null;
        }
        
        return user;
    }

    public void remove(final User user) {
        final Long id = user.getId();

        try {
            jdbcTemplate.update(DELETE_USER, id);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
        
    }

    public User getById(final Long id) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(GET_USER_BY_ID, new Object[] { id }, new UserRowMapper());
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("unregistred user!!!");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return user;

    }

    public User getUserByEmail(final String email) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(GET_USER_BY_EMAIL, new Object[] { email }, new UserRowMapper());
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("unregistred user!!!");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public Collection<User> getAll() {
        Collection<User> users = Collections.emptyList();
        try {
            users = jdbcTemplate.query(GET_ALL_USERS, new UserRowMapper());
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("Table users is empty");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    public void addSystemMsg(final Long userId, final String message) {

        try {
            final Timestamp time = Timestamp.valueOf(LocalDateTime.now());
            jdbcTemplate.update(INSERT_USER_SYSTEM_MESSAGE, userId, message, time);
            System.out.format("Congratulations! You win free ticket. User id: %s system msg: %s; timestamp: %s \n", userId, message, time);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
    }

}
