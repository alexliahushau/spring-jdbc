package com.epam.spring.jdbc.service;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.User;

/**
 * @author Yuriy_Tkach
 */
public interface DiscountService {

    byte getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets);

}
