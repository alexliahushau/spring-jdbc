package com.epam.spring.jdbc.service.impl;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.service.DiscountStrategy;

/**
 * @author Aliaksandr_Liahushau
 */
@Service
public class DiscountStrategyPerTicket implements DiscountStrategy {
	
    final static String TYPE = "ticket.";
    final static String DISCOUNT = TYPE + "discount";
    final static String PER_TICKETS = TYPE + "perTickets";

    @Inject
    Environment env;

    private int perNumberOfTicket;
    private double ticketsDiscount;
	
    @Override
    public double getDiscount(final User user, final LocalDateTime airDateTime, final long numberOfTickets) {
        final double perTicketDiscount = ((numberOfTickets / (numberOfTickets - (ticketsDiscount / 100)) - 1) * 100);
        final double discount = numberOfTickets / perNumberOfTicket * perTicketDiscount;

        return discount;
	}
	
    @PostConstruct
    public void init() {
    	ticketsDiscount = env.getProperty(DISCOUNT) != null ? Double.parseDouble(env.getProperty(DISCOUNT)) : 1.0;
    	perNumberOfTicket = env.getProperty(PER_TICKETS) != null ? Integer.parseInt(env.getProperty(PER_TICKETS)) : 1;
    }

}
