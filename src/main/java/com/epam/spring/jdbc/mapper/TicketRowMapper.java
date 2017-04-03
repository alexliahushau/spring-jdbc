package com.epam.spring.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;

public class TicketRowMapper implements RowMapper<Ticket> {

    @Override
    public Ticket mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Long id = rs.getLong("TICKET_ID");
        final Long userId = rs.getLong("USER_ID");
        final Long eventId = rs.getLong("EVENT_ID");
        final LocalDateTime date_time = rs.getTimestamp("DATE_TIME").toLocalDateTime();
        final Long seat = rs.getLong("SEAT");
        final boolean purchased = rs.getBoolean("PURCHASED");

        final User user = new User();
        final Event event = new Event();
        
        user.setId(userId);
        event.setId(eventId);
        
        final Ticket ticket = new Ticket(user, event, date_time, seat);
        ticket.setId(id);
        ticket.setPurchased(purchased);

        return ticket;
    }
}
