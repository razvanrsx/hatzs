package com.example.simulator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SimulatorConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
