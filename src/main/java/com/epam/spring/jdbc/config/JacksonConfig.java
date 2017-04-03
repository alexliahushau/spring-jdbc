package com.epam.spring.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {
    
	@Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        final JavaTimeModule module = new JavaTimeModule();
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializationInclusion(Include.NON_NULL)
                .findModulesViaServiceLoader(true)
                .modulesToInstall(module);
    }
	
}
