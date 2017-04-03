package com.epam.spring.jdbc.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.service.DiscountService;
import com.epam.spring.jdbc.service.DiscountStrategy;

/**
 * @author Aliaksandr_Liahushau
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    @Inject
    private ApplicationContext cxt;

    private List<DiscountStrategy> strategies;
	
	@Override
	public byte getDiscount(final User user, final Event event, final LocalDateTime airDateTime, final long numberOfTickets) {
        final double discount = strategies.stream()
                .map(strategy -> strategy.getDiscount(user, airDateTime, numberOfTickets))
                .max((v1, v2) -> v1.compareTo(v2)).get();
        final long ticketsBasePrice = (long) (event.getBasePrice() * numberOfTickets);

        return (byte) (ticketsBasePrice * discount / 100);
	}

	@PostConstruct
    public void init() {
        strategies = cxt.getBeansOfType(DiscountStrategy.class).values().stream().collect(Collectors.toList());
    }
	
}
