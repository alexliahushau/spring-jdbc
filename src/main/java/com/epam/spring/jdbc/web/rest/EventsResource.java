package com.epam.spring.jdbc.web.rest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.security.AuthoritiesConstants;
import com.epam.spring.jdbc.service.BookingService;
import com.epam.spring.jdbc.service.EventService;
import com.epam.spring.jdbc.service.UserService;

/**
 * REST controller for managing events.
 */
@RestController
@RequestMapping("/api/v1")
public class EventsResource {

        private static final Logger LOG = LoggerFactory.getLogger(EventsResource.class);

        @Inject
        private EventService eventService;

        @Inject
        private UserService userService;

        @Inject
        private BookingService bookingService;


        @Secured(AuthoritiesConstants.USER)
        @RequestMapping(value = "/events",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> getallEvents() {

            LOG.info("REST request to get all events");

            return new ResponseEntity<>(eventService.getAll(), HttpStatus.OK);
            
        }


        @Secured(AuthoritiesConstants.USER)
        @RequestMapping(value = "/events/{id}",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> getEventbyId(@PathVariable final Long id) {

            LOG.info("REST request to get event by id={}", id);

            return new ResponseEntity<>(eventService.getById(id), HttpStatus.OK);
            
        }


        @Secured(AuthoritiesConstants.USER)
        @RequestMapping(value = "/seats",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> getEventFreeSeats(
                @RequestParam final Long id,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime airDate) {

            LOG.info("REST request to get free seats for event id={} on air date={}", id, airDate);
            
            return new ResponseEntity<>(bookingService.getFreeSeats(id, airDate), HttpStatus.OK);
            
        }


        @Secured(AuthoritiesConstants.USER)
        @RequestMapping(value = "/seats/price",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> getEventSeatsPrice(
                final Principal principal,
                @RequestParam final Long eventId,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime airDate,
                @RequestParam final Set<Long> seats) {

            LOG.info("REST request to get seats price for event id={}, user={}, on air date={}, seats={}", eventId, principal.getName(), airDate, seats);

            final User user = userService.getUserByEmail(principal.getName());
            final Event event = eventService.getById(eventId);

            return new ResponseEntity<>(bookingService.getTicketsPrice(event, airDate, user, seats), HttpStatus.OK);

        }
}
