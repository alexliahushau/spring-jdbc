package com.epam.spring.jdbc.service;

import java.time.LocalDateTime;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;

/**
 * @author Yuriy_Tkach
 */
public interface BookingService {

    /**
     * Getting price when buying all supplied seats for particular event
     * 
     * @param event
     *            Event to get base ticket price, vip seats and other
     *            information
     * @param dateTime
     *            Date and time of event air
     * @param user
     *            User that buys ticket could be needed to calculate discount.
     *            Can be <code>null</code>
     * @param seats
     *            Set of seat numbers that user wants to buy
     * @return total price
     */
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user,
            @Nonnull Set<Long> seats);

    /**
     * Books tickets in internal system. If user is not
     * <code>null</code> in a ticket then booked tickets are saved with it
     * 
     * @param tickets
     *            Set of tickets
     */
    public Set<Ticket> bookTickets(@Nonnull Set<Ticket> tickets);

    /**
     * Getting all purchased tickets for event on specific air date and time
     * 
     * @param event
     *            Event to get tickets for
     * @param dateTime
     *            Date and time of airing of event
     * @return set of all purchased tickets
     */
    public @Nonnull Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime);

    /**
     * Getting all booked tickets for event on specific air date and time
     * 
     * @param event
     *            Event to get tickets for
     * @param dateTime
     *            Date and time of airing of event
     * @return set of all booked tickets
     */
    public @Nonnull Set<Ticket> getBookedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime);

    public Set<Long> getFreeSeats(Long id, LocalDateTime airDate);

    public @Nonnull Set<Ticket> getUserBookedTickets(@Nonnull User user);

    public @Nonnull Set<Ticket> buyTickets(@Nonnull Set<Ticket> tickets);

    public @Nonnull Ticket getTicketById(@Nonnull long id);
}
