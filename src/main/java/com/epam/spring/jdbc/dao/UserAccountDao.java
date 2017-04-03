package com.epam.spring.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.epam.spring.jdbc.domain.UserAccount;
import com.epam.spring.jdbc.mapper.UserAccountRowMapper;

/**
 * @author Aliaksandr_Liahushau
 */
@Component
public class UserAccountDao {
    final static String SAVE_USER_ACCOUNT = "INSERT INTO USER_ACCOUNT (USER_ID, AMOUNT) VALUES (?,?)";
    final static String UPDATE_ACCOUNT = "UPDATE USER_ACCOUNT SET AMOUNT = ? WHERE ACCOUNT_ID = ? AND USER_ID = ?";
    final static String GET_ALL_ACCOUNTS = "SELECT * FROM USER_ACCOUNT";
    final static String GET_USER_ACCOUNT_BY_USER_ID = GET_ALL_ACCOUNTS + " WHERE USER_ID = ?";
    
    @Inject
    private JdbcTemplate jdbcTemplate;
    
    public UserAccount save(UserAccount userAccount) {
    	final Long id = userAccount.getId();
    	final Long userId = userAccount.getUserId();
    	final Double amount = userAccount.getAmount();
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        
        try {
            if (id == null) {
                jdbcTemplate.update((PreparedStatementCreator) conn -> {
                    final PreparedStatement ps = conn.prepareStatement(SAVE_USER_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, userId);
                    ps.setDouble(2, amount);
                    return ps;
                } , keyHolder);
                userAccount.setId(keyHolder.getKey().longValue());
            } else {
                jdbcTemplate.update(UPDATE_ACCOUNT, amount, id, userId);
            }
            
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
            userAccount = null;
        }
        
        return userAccount;
    }

    public UserAccount getById(final Long userId) {
        UserAccount userAccount = null;
        try {
        	userAccount = jdbcTemplate.queryForObject(GET_USER_ACCOUNT_BY_USER_ID, new Object[] { userId }, new UserAccountRowMapper());
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("cuurent user do not have account!!!");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return userAccount;

    }

    public Collection<UserAccount> getAll() {
        Collection<UserAccount> userAccounts = Collections.emptyList();
        try {
        	userAccounts = jdbcTemplate.query(GET_ALL_ACCOUNTS, new UserAccountRowMapper());
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("Table USER_ACCOUNT is empty");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return userAccounts;
    }

}
