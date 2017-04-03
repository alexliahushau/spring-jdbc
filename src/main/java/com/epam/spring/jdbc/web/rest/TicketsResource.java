package com.epam.spring.jdbc.web.rest;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.security.AuthoritiesConstants;
import com.epam.spring.jdbc.service.BookingService;
import com.epam.spring.jdbc.service.EventService;
import com.epam.spring.jdbc.service.UserService;
import com.epam.spring.jdbc.web.dto.BookTicketsDTO;

/**
 * REST controller for managing tickets.
 */
@RestController
@RequestMapping("/api/v1")
public class TicketsResource {

    private static final Logger LOG = LoggerFactory.getLogger(TicketsResource.class);

    @Inject
    private EventService eventService;

    @Inject
    private BookingService bookingService;

    @Inject
    private UserService userService;

    @Secured(AuthoritiesConstants.USER)
    @RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bookTickets(final Principal principal, @RequestBody final BookTicketsDTO dto) {

        LOG.info("REST request to book tickets for event id={}, user={}, on air date={}, seats={}", dto.getEventId(),
                principal.getName(), dto.getAirDate(), dto.getSeats());
        final User user = userService.getUserByEmail(principal.getName());
        final Event event = eventService.getById(dto.getEventId());
        final Set<Ticket> ticketsToBook = dto.getSeats().stream()
                .map(seat -> new Ticket(user, event, dto.getAirDate(), seat)).collect(Collectors.toSet());

        Set<Ticket> tickets = bookingService.bookTickets(ticketsToBook);

        return CollectionUtils.isNotEmpty(tickets) ? new ResponseEntity<>(tickets, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Secured(AuthoritiesConstants.USER)
    @RequestMapping(value = "/buyTickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyTickets(final Principal principal, @RequestBody final Set<Ticket> tickets) {

        LOG.info("REST request to buy tickets for user={}, tickets={}", principal.getName(), tickets);

        final Set<Ticket> purchasedTickets = bookingService.buyTickets(tickets);

        return CollectionUtils.isNotEmpty(purchasedTickets) ? new ResponseEntity<>(purchasedTickets, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Secured(AuthoritiesConstants.USER)
    @RequestMapping(value = "/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookedTickets() {
        final String name = SecurityContextHolder.getContext().getAuthentication().getName();
        LOG.info("REST request to get user={} booked tickets", name);

        final User user = userService.getUserByEmail(name);
        final Set<Ticket> tickets = bookingService.getUserBookedTickets(user);

        return CollectionUtils.isNotEmpty(tickets) ? new ResponseEntity<>(tickets, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(method=RequestMethod.GET, value="/tickets/{id}", produces="application/pdf")
    public @ResponseBody Ticket getTicket(@PathVariable final long id) {
        return bookingService.getTicketById(id);
    }
}
