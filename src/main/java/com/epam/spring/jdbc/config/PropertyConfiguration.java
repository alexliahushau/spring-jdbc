package com.epam.spring.jdbc.config;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

@Configuration
public class PropertyConfiguration {

	@Inject
	ConfigurableEnvironment env;

	@PostConstruct
	public void setup() throws IOException {
		env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("/config/web.properties")));
		env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("/config/auditorium.properties")));
		env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("/config/discount.properties")));
		env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("/config/views.properties")));
	}

}
