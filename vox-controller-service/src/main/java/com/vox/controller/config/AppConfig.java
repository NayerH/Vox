package com.vox.controller.config;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JsonNodeFactory jsonNodeFactory() {
        return JsonNodeFactory.instance;
    }
}
