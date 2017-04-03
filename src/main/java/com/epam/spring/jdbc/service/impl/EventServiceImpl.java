package com.epam.spring.jdbc.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.epam.spring.jdbc.dao.AuditoriumDao;
import com.epam.spring.jdbc.dao.EventDao;
import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.service.EventService;

/**
 * @author Aliaksandr_Liahushau
 */
@Service
public class EventServiceImpl implements EventService {

	@Inject
	EventDao eventDao;
	
	@Inject
	AuditoriumDao auditoriumDao;

	@Override
	public Event save(Event event) {
		return eventDao.save(event);
	}

	@Override
	public void remove(Event event) {
		eventDao.remove(event);		
	}

	@Override
	public Event getById(Long id) {
		
		return setAuditoriums(eventDao.getById(id));
	}

	@Override
	public Collection<Event> getAll() {
		return eventDao.getAll().stream().map(e-> setAuditoriums(e)).collect(Collectors.toList());
	}

	@Override
	public Event getByName(String name) {
		return setAuditoriums(eventDao.getByName(name));
	}

    @Override
    public Set<Event> getForDateRange(final LocalDate from, final LocalDate to) {
        return eventDao.getAll().stream()
                .filter(isInDateRange(from, to))
                .map(e -> setAuditoriums(e))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getNextEvents(final LocalDateTime to) {
        final LocalDate today = LocalDate.now();
        return eventDao.getAll().stream()
                .filter(isInDateRange(today, to.toLocalDate()))
                .map(e -> setAuditoriums(e))
                .collect(Collectors.toSet());
    }

    private Predicate <Event> isInDateRange(LocalDate from, LocalDate to) {
        return event -> event.getAirDates().stream()
                .anyMatch(date -> date.isAfter(from.atStartOfDay().minusDays(1)) && date.isBefore(to.atStartOfDay().plusDays(1)));

    }

    private Event setAuditoriums(final Event event) {
		event.getAuditoriums().entrySet().stream()
				.forEach(a -> a.setValue(auditoriumDao.getByName(a.getValue().getName())));
    	
		return event;
	}
    
}
