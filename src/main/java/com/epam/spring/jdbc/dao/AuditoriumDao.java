package com.epam.spring.jdbc.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.epam.spring.jdbc.domain.Auditorium;
import com.epam.spring.jdbc.mapper.AuditoriumRowMapper;

/**
 * @author Aliaksandr_Liahushau
 */
@Repository
public class AuditoriumDao {

	final static String NAME = ".name";
	final static String SEATS = ".seats";
	final static String VIP_SEATS = ".vipSeats";

    final static String SAVE_AUDITORIUM = "INSERT INTO AUDITORIUMS (AUDITORIUM_NAME, SEATS) VALUES (?,?)";
    final static String SAVE_AUDITORIUM_VIP_SEATS = "INSERT INTO AUDITORIUM_VIP_SEATS (AUDITORIUM_NAME, VIP_SEAT) VALUES (?,?)";

    final static String GET_ALL_AUDITORIUMS = "SELECT * FROM AUDITORIUMS NATURAL JOIN AUDITORIUM_VIP_SEATS";
    final static String GET_AUDITORIUM_BY_NAME = "SELECT * FROM AUDITORIUMS NATURAL JOIN AUDITORIUM_VIP_SEATS WHERE AUDITORIUM_NAME = ?";
    
    @Inject
	Environment env;
	
    @Inject
    private JdbcTemplate jdbcTemplate;

    public Auditorium save(final Auditorium auditorium) {
        final String name = auditorium.getName();
        final Long seats = auditorium.getSeats();
        final Set<Long> vipSeats = auditorium.getVipSeats();

        try {
            jdbcTemplate.update(SAVE_AUDITORIUM, new Object[] { name, seats });
            if (vipSeats != null) {
                for (final Long seat : vipSeats) {
                    jdbcTemplate.update(SAVE_AUDITORIUM_VIP_SEATS, new Object[] { name, seat });
                }
            }
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }

        return auditorium;
    }

    public Auditorium getByName(final String name) {
        Auditorium auditorium = null;
        
        try {
        	auditorium = jdbcTemplate.query(GET_AUDITORIUM_BY_NAME, new Object[] { name }, new AuditoriumRowMapper())
            		.stream().collect(Collectors.toSet()).stream().findFirst().orElse(null);
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
        
        return auditorium;
	}

	public Set<Auditorium> getAll() {
	    Set<Auditorium> auditoriums = Collections.emptySet();
	    try {
	        auditoriums = jdbcTemplate.query(GET_ALL_AUDITORIUMS, new AuditoriumRowMapper()).stream().collect(Collectors.toSet()); 
	    } catch (final DataAccessException ex) {
            ex.printStackTrace();
        }
	    
	    return auditoriums;
	}
	
	@PostConstruct
	public void init() {
		boolean hasNext = true;
		int i = 1;
		
		while(hasNext) {
			if (env.containsProperty("a" + i + NAME)) {
				final String name = env.getProperty("a" + i + NAME);
				final Long seats = Long.parseLong(env.getProperty("a" + i + SEATS));
				final Set<Long> vipSeats = Arrays.asList(env.getProperty("a" + i + VIP_SEATS).split("\\s*,\\s*")).stream()
					.map(str -> Long.parseLong(str))
					.collect(Collectors.toSet());
                save(new Auditorium(name, seats, vipSeats));
				i++;
			} else {
				hasNext=false;
			}
		}
	}
	
	
}
