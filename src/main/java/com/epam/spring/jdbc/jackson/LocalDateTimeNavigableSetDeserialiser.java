package com.epam.spring.jdbc.jackson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LocalDateTimeNavigableSetDeserialiser extends JsonDeserializer<NavigableSet<LocalDateTime>> {

	    @Override
		public NavigableSet<LocalDateTime> deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			
	    	final ObjectMapper mapper = new ObjectMapper();
	        final JsonNode node = mapper.readTree(p);
	        final NavigableSet<LocalDateTime> tree = new TreeSet<>();
	        final Iterator<JsonNode> i = node.iterator();
	        
	        while (i.hasNext()) {
	        	JsonNode nodeObject = (JsonNode) i.next();
	        	tree.add(LocalDateTime.parse(nodeObject.asText()));
			}
	        
	        return tree;
		}
	}