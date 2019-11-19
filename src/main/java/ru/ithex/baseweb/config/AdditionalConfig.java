package ru.ithex.baseweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdditionalConfig {
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
