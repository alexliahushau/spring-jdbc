package com.epam.spring.jdbc.dao;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AspectEventMethodDao {
    final static String INSERT = "INSERT INTO EVENT_STATISTIC_COUNT (EVENT_ID, METHOD_NAME, COUNT) VALUES (?,?,?)";
    final static String UPDATE = "UPDATE EVENT_STATISTIC_COUNT SET COUNT = ? WHERE (EVENT_ID = ? AND METHOD_NAME = ?)";

    final static String GET = "SELECT COUNT FROM EVENT_STATISTIC_COUNT WHERE (EVENT_ID = ? AND METHOD_NAME = ?)";

    @Inject
    JdbcTemplate jdbcTemplate;

    public void addData(final Long key1, final String key2) {
        final Long count = getData(key1, key2);
        if (count != null) {
            updateData(key1, key2, count + 1);
            System.out.println(key1 + "   " + key2 + "  " + (count + 1));
        } else {
            saveData(key1, key2);
        }
    }

    private void saveData(final Long key1, final String key2) {

        try {
            jdbcTemplate.update(INSERT, key1, key2, 1);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    private void updateData(final Long key1, final String key2, final Long count) {

        try {
            jdbcTemplate.update(UPDATE, count, key1, key2);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    private Long getData(final Long key1, final String key2) {
        Long count = null;
        try {
            count = jdbcTemplate.queryForObject(GET, new Object[] {key1, key2}, Long.class);
        } catch (final EmptyResultDataAccessException ex) {

        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return count;
    }
}
