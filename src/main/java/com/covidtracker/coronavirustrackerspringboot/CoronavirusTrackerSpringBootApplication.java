package com.covidtracker.coronavirustrackerspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoronavirusTrackerSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoronavirusTrackerSpringBootApplication.class, args);
    }

}
