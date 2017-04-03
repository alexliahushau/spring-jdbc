package com.epam.spring.jdbc.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.epam.spring.jdbc.Application;
import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest
public class TestUserService {

    User user;

    @Inject
    private UserService userService;

    @Before
    public void executedBeforeEach() {
        final Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
    	user = new User();
        user.setFirstName("tolic");
        user.setLastName("anabolic");
        user.setEmail("tolic@email.com");
        user.setBirthday(LocalDate.now());
        user.setPassword("123");
        user.setRoles(roles);
    }

    @Test
    public void assertThatUserMustExist() {
        final User user = userService.getById(1L);
        assertEquals(user.getId().longValue(), 1L);
    }

    @Test
    public void saveUserTest() {
        final User newUser = userService.save(user);
        final Long newId = newUser.getId();

        assertEquals(user, userService.getById(newId));
    }

    @Test
    public void removeUserTest() {
        final Long userId = 1L;
        final User user = new User();
        
        user.setId(userId);
        userService.remove(user);

        assertEquals(null, userService.getById(userId));
    }

}
