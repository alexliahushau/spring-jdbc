package com.epam.spring.jdbc.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yuriy_Tkach
 */
public class Ticket extends DomainObject implements Comparable<Ticket> {

    private User user;

    private Event event;

    private LocalDateTime dateTime;

    private Long seat;
    
    private boolean purchased;
    
    public Ticket() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Ticket(final User user, final Event event, final LocalDateTime dateTime, final long seat) {
        this.user = user;
        this.event = event;
        this.dateTime = dateTime;
        this.seat = seat;
        purchased = false;
    }
    
    
    
    public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public Event getEvent() {
		return event;
	}



	public void setEvent(Event event) {
		this.event = event;
	}



	public LocalDateTime getDateTime() {
		return dateTime;
	}



	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}



	public Long getSeat() {
		return seat;
	}



	public void setSeat(Long seat) {
		this.seat = seat;
	}



	public boolean isPurchased() {
		return purchased;
	}



	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}



	@Override
    public int hashCode() {
        return Objects.hash(dateTime, event, seat);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ticket other = (Ticket) obj;
        if (dateTime == null) {
            if (other.dateTime != null) {
                return false;
            }
        } else if (!dateTime.equals(other.dateTime)) {
            return false;
        }
        if (event == null) {
            if (other.event != null) {
                return false;
            }
        } else if (!event.equals(other.event)) {
            return false;
        }
        if (seat != other.seat) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(final Ticket other) {
        if (other == null) {
            return 1;
        }
        int result = dateTime.compareTo(other.getDateTime());

        if (result == 0) {
            result = event.getId().compareTo(other.getEvent().getId());
        }
        if (result == 0) {
            result = Long.compare(seat, other.getSeat());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Ticket[id:" + this.getId() + ", user:" + user.getEmail() + ", event:"  + event + ", dateTime:" + dateTime
                + ", seat:" + seat + ", purchased:"
                + purchased + "]";
    }

}
