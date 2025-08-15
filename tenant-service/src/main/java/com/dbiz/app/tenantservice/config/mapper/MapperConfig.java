package com.dbiz.app.tenantservice.config.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
	
	@Bean
	public ObjectMapper objectMapperBean() {
		return new JsonMapper()
				.registerModule(new JavaTimeModule())
				.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	
	
}










