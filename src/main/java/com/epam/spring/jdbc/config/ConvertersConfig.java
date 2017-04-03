package com.epam.spring.jdbc.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.epam.spring.jdbc.config.converter.TicketToPDFConverter;

@Configuration
public class ConvertersConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		converters.add(new TicketToPDFConverter());
 
        super.configureMessageConverters(converters);
    }
}
