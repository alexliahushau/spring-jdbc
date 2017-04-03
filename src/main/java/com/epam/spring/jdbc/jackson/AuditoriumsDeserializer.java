package com.epam.spring.jdbc.jackson;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class AuditoriumsDeserializer extends KeyDeserializer {

	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return LocalDateTime.parse(key);
		} catch (Exception ex) {
			throw ctxt.weirdKeyException(LocalDateTime.class, key, ex.getMessage());
		}

	}

}
