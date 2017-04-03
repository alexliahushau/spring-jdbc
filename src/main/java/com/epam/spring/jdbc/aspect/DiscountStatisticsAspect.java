package com.epam.spring.jdbc.aspect;

import javax.inject.Inject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.epam.spring.jdbc.dao.AspectDiscountDao;
import com.epam.spring.jdbc.domain.User;

@Aspect
@Component
public class DiscountStatisticsAspect {

    @Inject
    AspectDiscountDao aspectDiscountDao;
    
    @Pointcut("execution(* com.epam.spring.jdbc.service.DiscountStrategy.*(..))")
    private void discountServiceOperation() {
    }

    @Pointcut("discountServiceOperation() && execution(* getDiscount(..))")
    private void getDiscount() {
    }
    
    @AfterReturning(pointcut = "getDiscount()", returning = "result")
    public void countGivenDiscount(final JoinPoint jp, final double result) {
        if (result > 0) {
            final String className = jp.getTarget().getClass().getName();
            Long userId = null;
            
            for (final Object obj : jp.getArgs()) {
                if (User.class.isInstance(obj)) {
                    userId = ((User) obj).getId();
                }
            }
            
            if (userId != null) {
                aspectDiscountDao.addData(userId, className);
            }
            
        }
    }


}
