package com.epam.spring.jdbc.web.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.domain.UserAccount;
import com.epam.spring.jdbc.service.UserAccountService;
import com.epam.spring.jdbc.service.UserService;
import com.epam.spring.jdbc.web.dto.UserDTO;
import com.epam.spring.jdbc.web.enums.UiViews;

@Controller
@RequestMapping("/user")
public class UsersController {

    private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);

    @Inject
    UserService userService;

    @Inject
    UserAccountService userAccountService;

    @RequestMapping( method = RequestMethod.GET)
    public ModelAndView getAllUsers(final Principal principal) {
        LOG.info("Enter users controller");

        final ModelAndView model = new  ModelAndView(UiViews.USERS.name(), "users", userService.getAll());
        model.addObject("principal", principal);

        return model;
    }

    @RequestMapping( value="/{id}", method = RequestMethod.GET)
    public ModelAndView getUserById(@PathVariable final Long id, final Principal principal) {
        LOG.info("Enter users controller get user by id={}", id);
        final User user = userService.getById(id);
        final UserAccount account = userAccountService.getUserAccountByUserId(id);
        final UserDTO dto = new UserDTO(user, account);
        final ModelAndView model = new  ModelAndView(UiViews.USER.name(), "dto", dto);

        model.addObject("principal", principal);

        model.setViewName(UiViews.USER.name());

        return model;
    }

}
