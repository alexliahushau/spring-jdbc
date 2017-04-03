package com.epam.spring.jdbc.service;

import java.time.LocalDateTime;

import javax.annotation.Nullable;

import com.epam.spring.jdbc.domain.User;

/**
 * @author Aliaksandr_Liahushau
 */
public interface DiscountStrategy {

    double getDiscount(@Nullable User user, @Nullable LocalDateTime airDateTime, @Nullable long numberOfTickets);

}
