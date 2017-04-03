package com.epam.spring.jdbc.web.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.security.AuthoritiesConstants;
import com.epam.spring.jdbc.service.BookingService;
import com.epam.spring.jdbc.service.EventService;
import com.epam.spring.jdbc.service.UserService;
import com.epam.spring.jdbc.web.enums.UiViews;

@Controller
@RequestMapping("/booking")
public class BookingController {

    private static final Logger LOG = LoggerFactory.getLogger(BookingController.class);

    @Inject
    Environment env;

    @Inject
    BookingService bookingService;

    @Inject
    UserService userService;

    @Inject
    EventService eventService;

    @Secured(AuthoritiesConstants.USER)
    @RequestMapping( method = RequestMethod.GET)
    public ModelAndView bookTickets(final Principal principal) {
        LOG.info("Enter BookingController bookTickets()");
        
        final ModelAndView model = new ModelAndView(UiViews.BOOKING.name(), "principal", principal);
        
        return model;
    }

    @Secured(AuthoritiesConstants.USER)
    @RequestMapping( value = "/tickets", method = RequestMethod.GET)
    public ModelAndView getTicketPDF(final Principal principal) {

        final User user = userService.getUserByEmail(principal.getName());
        final Set<Ticket> tickets = bookingService.getUserBookedTickets(user);
        final ModelAndView model = new ModelAndView("tickets", "user", user);
        model.addObject("principal", principal);
        model.addObject("tickets", tickets);

        return model;
    }

    @Secured(AuthoritiesConstants.BOOKING_MANAGER)
    @RequestMapping( value = "/event", method = RequestMethod.GET)
    public ModelAndView getEventTickets(
            @RequestParam final Long eventId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime airDate,
            final Principal principal) {

        final Event event = eventService.getById(eventId);
        final Set<Ticket> tickets = bookingService.getBookedTicketsForEvent(event, airDate);
        final ModelAndView model = new ModelAndView("tickets", "tickets", tickets);
        model.addObject("event", event);
        model.addObject("principal", principal);
        model.addObject("tickets", tickets);

        return model;
    }
    

}
