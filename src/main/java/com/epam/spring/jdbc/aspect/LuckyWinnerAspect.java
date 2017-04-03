package com.epam.spring.jdbc.aspect;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.epam.spring.jdbc.dao.UserDao;
import com.epam.spring.jdbc.domain.Ticket;

@Aspect
@Component
public class LuckyWinnerAspect {

    @Inject
    UserDao userDao;

    @Pointcut("execution(* com.epam.spring.jdbc.dao.TicketDao.*(..))")
    private void ticketDaoOperation() {
    }

    @Pointcut("ticketDaoOperation() && execution(* save(..))")
    private void saveTickets() {
    }

    @Around("saveTickets() && args(tickets)")
    public Object checkLuckyWinner(final ProceedingJoinPoint jp, final Set<Ticket> tickets) throws Throwable {
        if (!tickets.isEmpty()) {
            final Set<Ticket> newTickets = new HashSet<>();
            for (final Ticket ticket : tickets) {
                if (checkLuck()) {
                    ticket.setPurchased(true);
                    final Long userId = ticket.getUser().getId();
                    userDao.addSystemMsg(userId, "LYCKY WINNER");
                }
                newTickets.add(ticket);
            }
            final Object[] newArgs = new Object[1];
            newArgs[0] = newTickets;
            return jp.proceed(newArgs);
        }
		return jp.proceed();
    }

    private boolean checkLuck() {
        final int luckyNumber = 77;
        final int random = (int) (Math.random() * 100);

        return luckyNumber == random;
    }
}
