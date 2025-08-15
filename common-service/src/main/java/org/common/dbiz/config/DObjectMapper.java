package org.common.dbiz.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DObjectMapper {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
