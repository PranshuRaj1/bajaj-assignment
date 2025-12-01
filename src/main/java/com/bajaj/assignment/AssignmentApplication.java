package com.bajaj.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main application class for the Bajaj Assignment.
 * This application triggers a webhook generation and submission flow on startup.
 */
@SpringBootApplication
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }

    /**
     * Bean definition for RestTemplate to be used for API calls.
     * 
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
