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
public class DiscountStrategyBirthday implements DiscountStrategy {
	
    final static String TYPE = "birthday.";
    final static String DISCOUNT = TYPE + "discount";
    final static String DATE_RANGE = TYPE + "dateRange";
    
    @Inject
    Environment env;

    private double discount;
    private short dateRange;
	
	@Override
    public double getDiscount(final User user, final LocalDateTime airDateTime, final long numberOfTickets) {
        final LocalDateTime userBirthday = user.getBirthday().atStartOfDay();
        double result = 0;
        if (userBirthday != null
                && (userBirthday.isAfter(airDateTime.minusDays(dateRange + 1)) && userBirthday.isBefore(airDateTime.plusDays(1)))) {
            result = discount;
        }

        return result;
	}
	
	@PostConstruct
    public void init() {
        discount = env.getProperty(DISCOUNT) != null ? Double.parseDouble(env.getProperty(DISCOUNT)) : 1.0;
        dateRange = env.getProperty(DATE_RANGE) != null ? Short.parseShort(env.getProperty(DATE_RANGE)) : 1;
    }
	
}
