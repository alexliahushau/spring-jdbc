package com.epam.spring.jdbc.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.epam.spring.jdbc.dao.AuditoriumDao;
import com.epam.spring.jdbc.dao.EventDao;
import com.epam.spring.jdbc.dao.TicketDao;
import com.epam.spring.jdbc.dao.UserAccountDao;
import com.epam.spring.jdbc.dao.UserDao;
import com.epam.spring.jdbc.domain.Auditorium;
import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.EventRating;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.domain.UserAccount;
import com.epam.spring.jdbc.service.BookingService;
import com.epam.spring.jdbc.web.rest.errors.CustomParameterizedException;
import com.epam.spring.jdbc.web.rest.errors.ErrorConstants;

/**
 * @author Aliaksandr_Liahushau
 */
@Transactional
@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Inject
    TicketDao ticketDao;

    @Inject
    UserDao userDao;

    @Inject
    EventDao eventDao;

    @Inject
    AuditoriumDao auditoriumDao;

    @Inject
    UserAccountDao userAccountDao;

    @Override
    public double getTicketsPrice(final Event event, final LocalDateTime dateTime, final User user, final Set<Long> seats) {
        final Auditorium au = event.getAuditoriums().get(dateTime);
        final EventRating eventRating = event.getRating();

        final double rateMultyplier = eventRating == EventRating.HIGH ? 1.2 : 1;
        final double price = event.getBasePrice() * rateMultyplier;
        return seats.stream()
                .map(seat -> au.getVipSeats().contains(seat) ? price * 2 : price)
                .mapToDouble(u -> u).sum();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Set<Ticket> bookTickets(final Set<Ticket> tickets) {
        final Set<Ticket> validTickets = tickets.stream()
                .filter(isTicketOfRegistredUser())
                .filter(isTicketNotBooked())
            .collect(Collectors.toSet());
        return ticketDao.save(validTickets);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Set<Ticket> buyTickets(final Set<Ticket> tickets) {
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        final User user = userDao.getUserByEmail(userName); 
        final UserAccount account = userAccountDao.getById(user.getId());
        final Set<Ticket> validTickets = validateTicketsBeforePurchase(tickets);
        final Double sum = validTickets.stream().mapToDouble(t -> {
            final Set<Long> seats = new HashSet<Long>();
            seats.add(t.getSeat());
            t.setPurchased(true);
            t.setUser(user);
            return getTicketsPrice(t.getEvent(), t.getDateTime(), user, seats);
        }).sum();

        if (account.getAmount() < sum) {
            LOG.error("Not enouth money, account: {}, tickets price: {}", account.getAmount(), sum);
            throw new CustomParameterizedException(ErrorConstants.ERR_NOT_ENOUGTH_MONEY);
        }

        account.setAmount(account.getAmount() - sum);

        userAccountDao.save(account);

        return ticketDao.save(validTickets);
    }

    Predicate<Ticket> isTicketOfRegistredUser() {
        return ticket -> Optional.ofNullable(userDao.getById(ticket.getUser().getId())).isPresent();
    }

    Predicate<Ticket> isTicketNotBooked() {
        return ticket -> !ticketDao.getAll().stream().anyMatch(t -> t.equals(ticket));
    }

    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(final Event event, final LocalDateTime dateTime) {

        return ticketDao.getAll().stream().filter(t -> t.getEvent().getId() == event.getId()
                && t.getDateTime().equals(dateTime)
                && t.isPurchased()).collect(Collectors.toSet());
    }

    @Override
    public Set<Ticket> getBookedTicketsForEvent(final Event event, final LocalDateTime dateTime) {
        final Set<Ticket> tickets = ticketDao.getAll().stream().filter(t ->
                t.getEvent().getId() == event.getId()
                && t.getDateTime().equals(dateTime)
                && !t.isPurchased())
                .collect(Collectors.toSet());
        
        return setUsersAndEventsToTickets(tickets);
    }

    @Override
    public Set<Long> getFreeSeats(final Long id, final LocalDateTime airDate) {

        final Event event = eventDao.getById(id);

        final Auditorium aud = auditoriumDao.getByName(event.getAuditoriums().get(airDate).getName());

        final Set<Ticket> purchasedTickets = getPurchasedTicketsForEvent(event, airDate);
        final Set<Ticket> bookedTickets = getBookedTicketsForEvent(event, airDate);

        final Set<Long> purchasedSeats = purchasedTickets.stream().map(t -> t.getSeat()).collect(Collectors.toSet());
        final Set<Long> bookedSeats = bookedTickets.stream().map(t -> t.getSeat()).collect(Collectors.toSet());
        final List<Long> occupiedSeats = new ArrayList<>(purchasedSeats);

        occupiedSeats.addAll(bookedSeats);

        return aud.getAllSeats().stream().filter(seat -> !occupiedSeats.contains(seat))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Ticket> getUserBookedTickets(final User user) {

        return setUsersAndEventsToTickets(ticketDao.getUserTickets(user.getId()));
        
    }
    
    private Set<Ticket> setUsersAndEventsToTickets(final Set<Ticket> tickets) {
        tickets.forEach(t -> {
            t.setUser(userDao.getById(t.getUser().getId()));
            final Event event = eventDao.getById(t.getEvent().getId());
            event.getAuditoriums().entrySet().stream().forEach(
                a-> a.setValue(auditoriumDao.getByName(a.getValue().getName()))
            );  
            t.setEvent(event);
        });

        return tickets;
    }

    private Set<Ticket> validateTicketsBeforePurchase(final Set<Ticket> tickets) {
        return tickets.stream().map(t-> {
            final Ticket ticket = ticketDao.getById(t.getId());
            return ticket.isPurchased() ? null : ticket;
        }).collect(Collectors.toSet());
    }

    @Override
    public Ticket getTicketById(long id) {
        return ticketDao.getById(id);
    }

}
