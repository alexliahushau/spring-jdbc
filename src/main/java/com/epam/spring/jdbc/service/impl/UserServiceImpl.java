package com.epam.spring.jdbc.service.impl;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.epam.spring.jdbc.dao.EventDao;
import com.epam.spring.jdbc.dao.TicketDao;
import com.epam.spring.jdbc.dao.UserDao;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.service.UserService;

/**
 * @author Aliaksandr_Liahushau
 */
@Service
public class UserServiceImpl implements UserService {

    @Inject
    UserDao userDao;
    
    @Inject
    TicketDao ticketDao;
    
    @Inject
    EventDao eventDao;

    @Override
    public User save(final User user) {
        final User newUser = userDao.save(user);

        if (newUser != null) {
            ticketDao.save(user.getTickets());
        }

        return newUser;
    }

    @Override
    public void remove(final User user) {
        userDao.remove(user);
    }

    @Override
    public User getById(final Long id) {
        final User user = userDao.getById(id);
        return setTickets(user);
    }

    @Override
    public Collection<User> getAll() {
        return userDao.getAll().stream().map(u -> setTickets(u)).collect(Collectors.toList());
    }

    @Override
    public User getUserByEmail(final String email) {
        final User user = userDao.getUserByEmail(email);
        return setTickets(user);
    }

    private User setTickets(final User user) {
        if (user != null) {
            final NavigableSet<Ticket> tickets = ticketDao.getUserTickets(user.getId());
            tickets.stream().forEach(t -> t.setEvent(eventDao.getById(t.getEvent().getId())));
            user.setTickets(tickets);
        }
        return user;
    }
}
