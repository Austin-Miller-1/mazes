package com.amw.sms;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic bean-configuration for application.
 */
@Configuration
public class AppConfiguration {
    /**
     * Get Random instance.
     * @return Instance of Random.
     */
    @Bean
    public Random getRandom(){
        return new Random();
    }
}
