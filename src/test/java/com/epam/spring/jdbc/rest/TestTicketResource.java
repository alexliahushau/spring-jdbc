package com.epam.spring.jdbc.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.epam.spring.jdbc.Application;
import com.epam.spring.jdbc.service.BookingService;
import com.epam.spring.jdbc.service.EventService;
import com.epam.spring.jdbc.service.UserService;
import com.epam.spring.jdbc.web.rest.TicketsResource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest
public class TestTicketResource {

	@Inject
    private BookingService bookingService;

	@Inject
    private EventService eventService;
	
	@Inject
    private UserService userService;
	
	private MockMvc restMvc;
	
	@Before
    public void setup() {
        final TicketsResource ticketsResource = new TicketsResource();
        ReflectionTestUtils.setField(ticketsResource, "bookingService", bookingService);
        ReflectionTestUtils.setField(ticketsResource, "eventService", eventService);
        ReflectionTestUtils.setField(ticketsResource, "userService", userService);
        
        this.restMvc = MockMvcBuilders.standaloneSetup(ticketsResource)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
            .build();
    }

    /** MediaType for JSON UTF8 */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    @Test
    @WithMockUser(username="user@user.ru")
    @WithUserDetails
    public void testGetUserTickets() throws Exception {

        restMvc.perform(get("/api/v1/tickets").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0]").exists());
    }

}