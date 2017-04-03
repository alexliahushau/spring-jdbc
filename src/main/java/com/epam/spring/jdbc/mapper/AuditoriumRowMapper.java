package com.epam.spring.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.jdbc.core.RowMapper;

import com.epam.spring.jdbc.domain.Auditorium;

public class AuditoriumRowMapper implements RowMapper<Auditorium> {

    private String name = "";
    private Set<Long> vipSeats;
    
    @Override
    public Auditorium mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Auditorium auditorium = new Auditorium();
        
        final String name = rs.getString("AUDITORIUM_NAME");
        final Long seats = rs.getLong("SEATS");
        final Long vipSeat = rs.getLong("VIP_SEAT");
        
        
        if (!this.name.equals(name)) {
            this.name = name;
            vipSeats = new HashSet<>();
        }
        
        vipSeats.add(vipSeat);
        auditorium.setName(name);
        auditorium.setSeats(seats);
        auditorium.setVipSeats(vipSeats);

        return auditorium;
    }
}
