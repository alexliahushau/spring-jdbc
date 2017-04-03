package com.epam.spring.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.epam.spring.jdbc.domain.UserAccount;

public class UserAccountRowMapper implements RowMapper<UserAccount> {

    @Override
    public UserAccount mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final UserAccount userAccount = new UserAccount();
        
        userAccount.setId(rs.getLong("ACCOUNT_ID"));
        userAccount.setUserId(rs.getLong("USER_ID"));
        userAccount.setAmount(rs.getDouble("AMOUNT"));
        
        return userAccount;
    }
}
