package com.epam.spring.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.mapper.TicketRowMapper;

/**
 * @author Aliaksandr_Liahushau
 */
@Repository
public class TicketDao {
    final static String SAVE_TICKET = "INSERT INTO TICKETS (EVENT_ID, USER_ID, DATE_TIME, SEAT, PURCHASED) VALUES (?,?,?,?,?)";
    final static String UPDATE_TICKET = "UPDATE TICKETS SET EVENT_ID = ?, USER_ID = ?, DATE_TIME = ?, SEAT = ?, PURCHASED = ? WHERE TICKET_ID = ?";
    final static String GET_ALL_TICKETS = "SELECT * FROM TICKETS";
    final static String GET_TICKET_BY_ID = GET_ALL_TICKETS + " WHERE TICKET_ID = ?";
    final static String GET_TICKETS_BY_USER_ID = "SELECT * FROM TICKETS WHERE USER_ID = ?";

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    UserDao userDao;

    @Inject
    EventDao eventDao;

    @Inject
    AuditoriumDao auditoriumDao;

    public Set<Ticket> save(Set<Ticket> tickets) {
        if (!CollectionUtils.isEmpty(tickets)) {
            tickets = tickets.stream().map(t -> saveTicket(t)).collect(Collectors.toSet());
        }

        return tickets;
    }

    public Set<Ticket> getAll() {
        Set<Ticket> tickets = Collections.emptySet();

        try {
            tickets = jdbcTemplate.query(GET_ALL_TICKETS, new TicketRowMapper()).stream().collect(Collectors.toSet());
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return tickets;
    }

    public Ticket getById(final Long id) {
        Ticket ticket = null;
        try {
            ticket = jdbcTemplate.queryForObject(GET_TICKET_BY_ID, new Object[] { id }, new TicketRowMapper());
            ticket.setUser(userDao.getById(ticket.getUser().getId()));
            ticket.setEvent(eventDao.getById(ticket.getEvent().getId()));
            
        } catch (final EmptyResultDataAccessException ex) {
            System.out.println("Ticket not found");
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return ticket;

    }

    private Ticket saveTicket(Ticket ticket) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Long id = ticket.getId();
        final Long userId = ticket.getUser().getId();
        final Long eventId = ticket.getEvent().getId();
        final Timestamp date = Timestamp.valueOf(ticket.getDateTime());
        final Long seat = ticket.getSeat();
        final boolean purchased = ticket.isPurchased();

        try {
             if ( id == null) {   
                jdbcTemplate.update((PreparedStatementCreator) conn -> {
                    final PreparedStatement ps = conn.prepareStatement(SAVE_TICKET, Statement.RETURN_GENERATED_KEYS);
    
                    ps.setLong(1, eventId);
                    ps.setLong(2, userId);
                    ps.setTimestamp(3, date);
                    ps.setLong(4, seat);
                    ps.setBoolean(5, purchased);
          
                    return ps;
                } , keyHolder);
                ticket.setId(keyHolder.getKey().longValue());
            } else {
                jdbcTemplate.update(UPDATE_TICKET, eventId, userId, date, seat, purchased, id);
            }
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
            ticket = null;
        }

        return ticket;
    }
    
    public NavigableSet<Ticket> getUserTickets(final Long userId) {
        final NavigableSet<Ticket> tickets = new TreeSet<>();
        
        try {
            tickets.addAll(jdbcTemplate.query(GET_TICKETS_BY_USER_ID, new Object[] { userId }, new TicketRowMapper()).stream().collect(Collectors.toSet()));
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
        
        return tickets;
        
    }
}
