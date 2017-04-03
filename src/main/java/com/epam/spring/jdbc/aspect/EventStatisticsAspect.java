package com.epam.spring.jdbc.aspect;

import java.util.Set;

import javax.inject.Inject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.epam.spring.jdbc.dao.AspectEventMethodDao;
import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.Ticket;

@Aspect
@Component
public class EventStatisticsAspect {
    final static String QUERY_BY_NAME = "QUERY_BY_NAME";
    final static String QUERY_PRICE = "QUERY_PRICE";
    final static String QUERY_BOOK_TICKET = "QUERY_BOOK_TICKET";

    @Inject
    AspectEventMethodDao aspectDao;

    @Pointcut("execution(* com.epam.spring.jdbc.service.EventService.*(..))")
    private void eventServiceOperation() {
    }

    @Pointcut("execution(* com.epam.spring.jdbc.service.BookingService.*(..))")
    private void bookingServiceOperation() {
    }

    @Pointcut("eventServiceOperation() && execution(* getByName(..))")
    private void eventGetbyName() {
    }

    @Pointcut("bookingServiceOperation() && execution(* bookTickets(..))")
    private void bookTickets() {
    }

    @Pointcut("bookingServiceOperation() && execution(* getTicketsPrice(..))")
    private void getTicketsPrice() {
    }



    @AfterReturning(pointcut = "eventGetbyName()", returning = "event")
    public void countQueryEventGetbyName(final JoinPoint jp, final Event event) {
        if (event != null) {
            aspectDao.addData(event.getId(), QUERY_BY_NAME);
        }
    }

    @Before("getTicketsPrice() && args(event,..)")
    public void countQueryTicketsPrice(final JoinPoint jp, final Event event) {
        if (event != null) {
            aspectDao.addData(event.getId(), QUERY_PRICE);
        }
    }

    @Before("bookTickets() && args(tickets,..)")
    public void countQuerybookTickets(final JoinPoint jp, final Set<Ticket> tickets) {
        if (tickets != null) {
            final Ticket ticket = tickets.stream().findAny().orElse(null);
            if (ticket != null) {
                final Event event = ticket.getEvent();
                aspectDao.addData(event.getId(), QUERY_BOOK_TICKET);
            }
        }
    }
}
