package com.epam.spring.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.jdbc.core.RowMapper;

import com.epam.spring.jdbc.domain.User;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final User user = new User();
        
        user.setId(rs.getLong("USER_ID"));
        user.setFirstName(rs.getString("FIRST_NAME"));
        user.setLastName(rs.getString("LAST_NAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setBirthday(rs.getDate("BIRTHDAY") != null ? rs.getDate("BIRTHDAY").toLocalDate() : null);
        user.setPassword(rs.getString("PASSWORD"));
        user.setRoles(new HashSet<String>(Arrays.asList(rs.getString("ROLES").split("\\s*,\\s*"))));
        
        return user;
    }
}
