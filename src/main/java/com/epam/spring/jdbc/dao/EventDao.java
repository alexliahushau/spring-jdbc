package com.epam.spring.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.epam.spring.jdbc.domain.Auditorium;
import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.mapper.EventRowMapper;

/**
 * @author Aliaksandr_Liahushau
 */
@Repository
public class EventDao {

    final static String INSERT_INTO_EVENTS = "INSERT INTO EVENTS (NAME, BASE_PRICE, RATING) VALUES (?,?,?)";
    final static String INSERT_INTO_EVENTS_AUDITORIUMS_DATE = "INSERT INTO EVENT_AUDITORIUM_DATE (EVENT_ID, AUDITORIUM_NAME, DATE) VALUES (?,?,?)";
    final static String INSERT_INTO_EVENT_AIR_DATES = "INSERT INTO EVENT_AIR_DATES (EVENT_ID, AIR_DATE) VALUES (?,?)";

    final static String GET_ALL_EVENTS = "SELECT * FROM EVENTS";
    final static String GET_EVENT_BY_ID = GET_ALL_EVENTS + " WHERE EVENT_ID = ?";
    final static String GET_EVENT_BY_NAME = GET_ALL_EVENTS + " WHERE NAME = ?";

    final static String GET_EVENT_AIR_DATES = "SELECT * FROM EVENT_AIR_DATES WHERE EVENT_ID = ?";

    final static String GET_EVENT_AUDITORIUMS = "SELECT * FROM EVENT_AUDITORIUM_DATE WHERE EVENT_ID = ?";

    final static String DELETE_EVENT_BY_ID = "DELETE FROM EVENTS WHERE EVENT_ID = ?";
    final static String DELETE_EVENT_AIR_DATES = "DELETE FROM EVENT_AIR_DATES WHERE EVENT_ID = ?";
    final static String DELETE_EVENT_AUDITORIUMS_DATES = "DELETE FROM EVENT_AUDITORIUM_DATE WHERE EVENT_ID = ?";

    @Inject
    private JdbcTemplate jdbcTemplate;
    
    @Inject
    AuditoriumDao auditoriumDao;

    public Event save(final Event event) {
        final String name = event.getName();
        final Double basePrice = event.getBasePrice();
        final Set<LocalDateTime> airDates = event.getAirDates();
        final Map<LocalDateTime, Auditorium> auditoriums = event.getAuditoriums();
        final String rating = event.getRating().name();
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update((PreparedStatementCreator) conn -> {
                final PreparedStatement ps = conn.prepareStatement(INSERT_INTO_EVENTS, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setDouble(2, basePrice);
                ps.setString(3, rating);
                return ps;
            } , keyHolder);
            final Long eventId = keyHolder.getKey().longValue();
            event.setId(eventId);

            auditoriums.entrySet().stream().forEach(entry -> jdbcTemplate.update(INSERT_INTO_EVENTS_AUDITORIUMS_DATE,
                    eventId, entry.getValue().getName(), Timestamp.valueOf(entry.getKey())));
            airDates.stream().forEach(
                    date -> jdbcTemplate.update(INSERT_INTO_EVENT_AIR_DATES, eventId, Timestamp.valueOf(date)));
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return event;
    }

    public void remove(final Event event) {
        if (event != null) {
            try {
                final Long eventId = event.getId();
                jdbcTemplate.update(DELETE_EVENT_BY_ID, eventId);
                jdbcTemplate.update(DELETE_EVENT_AIR_DATES, eventId);
                jdbcTemplate.update(DELETE_EVENT_AUDITORIUMS_DATES, eventId);
            } catch (final DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Cannot delete event = NULL");
    }

    public Event getById(final Long id) {
        Event event = null;

        try {
            event = jdbcTemplate.queryForObject(GET_EVENT_BY_ID, new Object[] { id }, new EventRowMapper());
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
        if (event != null) {
            event.setAirDates(getAirDates(event.getId()));
            event.setAuditoriums(getAuditoriums(event.getId()));
        }

        return event;
    }

    public Event getByName(final String name) {
        Event event = null;

        try {
            event = jdbcTemplate.query(GET_EVENT_BY_NAME, new Object[] { name }, new EventRowMapper()).stream()
                    .collect(Collectors.toSet()).stream().findFirst().orElse(null);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
        if (event != null) {
            event.setAirDates(getAirDates(event.getId()));
            event.setAuditoriums(getAuditoriums(event.getId()));
        }

        return event;
    }

    public Collection<Event> getAll() {
        Set<Event> events = Collections.emptySet();

        try {
            events = jdbcTemplate.query(GET_ALL_EVENTS, new EventRowMapper()).stream().collect(Collectors.toSet());
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        events.stream().forEach(e -> {
            e.setAirDates(getAirDates(e.getId()));
            e.setAuditoriums(getAuditoriums(e.getId()));
        });

        return events;
    }

    private NavigableSet<LocalDateTime> getAirDates(final Long eventId) {
        final NavigableSet<LocalDateTime> airDates = new TreeSet<>();
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_EVENT_AIR_DATES, eventId);
            for (Map<String, ?> row : rows) {
                final LocalDateTime date = ((Timestamp) row.get("AIR_DATE")).toLocalDateTime();
                airDates.add(date);
            }
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return airDates;

    }

    private NavigableMap<LocalDateTime, Auditorium> getAuditoriums(final Long eventId) {
        final NavigableMap<LocalDateTime, Auditorium> auditoriums = new TreeMap<>();
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_EVENT_AUDITORIUMS, eventId);
            for (Map<String, ?> row : rows) {
                final LocalDateTime date = ((Timestamp) row.get("DATE")).toLocalDateTime();
                final Auditorium auditorium = auditoriumDao.getByName((String) row.get("AUDITORIUM_NAME"));
                auditoriums.put(date, auditorium);
            }
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return auditoriums;

    }
}
