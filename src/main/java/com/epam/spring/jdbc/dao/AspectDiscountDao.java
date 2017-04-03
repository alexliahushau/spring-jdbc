package com.epam.spring.jdbc.dao;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AspectDiscountDao {
    final static String INSERT_DISCOUNT = "INSERT INTO COUNT_GIVEN_DISCOUNT (USER_ID, CLASS_NAME, COUNT) VALUES (?,?,?)";
    final static String UPDATE_DISCOUNT = "UPDATE COUNT_GIVEN_DISCOUNT SET COUNT = ? WHERE (USER_ID = ? AND CLASS_NAME = ?)";

    final static String GET_DISCOUNT = "SELECT COUNT FROM COUNT_GIVEN_DISCOUNT WHERE (USER_ID = ? AND CLASS_NAME = ?)";

    final static String DISCOUNT_TABLE = "COUNT_GIVEN_DISCOUNT";
    final static String EVENT_METHOD_TABLE = "EVENT_STATISTIC_COUNT";

    @Inject
    JdbcTemplate jdbcTemplate;

    public void addData(final Long userId, final String className) {
        final Long count = getData(userId, className);
        if (count != null) {
            updateData(userId, className, count + 1);
            System.out.println(userId + "   " + className + "  " + (count + 1));
        } else {
            saveData(userId, className);
        }
    }

    private void saveData(final Long userId, final String className) {

        try {
            jdbcTemplate.update(INSERT_DISCOUNT, userId, className, 1);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    private void updateData(final Long userId, final String className, final Long count) {

        try {
            jdbcTemplate.update(UPDATE_DISCOUNT, count, userId, className);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    private Long getData(final Long userId, final String className) {
        Long count = null;
        try {
            count = jdbcTemplate.queryForObject(GET_DISCOUNT, new Object[] {userId, className}, Long.class);
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("Congratulations! You get your first discount.");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return count;
    }
}
