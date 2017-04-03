package com.epam.spring.jdbc.web.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class BookTicketsDTO {
	 
	private Long eventId;
	
	private LocalDateTime airDate;
	
	private Set<Long> seats;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(final Long eventId) {
		this.eventId = eventId;
	}

	public LocalDateTime getAirDate() {
		return airDate;
	}

	public void setAirDate(final LocalDateTime airDate) {
		this.airDate = airDate;
	}

	public Set<Long> getSeats() {
		return seats;
	}

	public void setSeats(final Set<Long> seats) {
		this.seats = seats;
	}
	
	
}
