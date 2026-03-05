package com.example.flightstatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(SimulationProperties.class)
@EnableScheduling
public class FlightStatusSimulatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightStatusSimulatorApplication.class, args);
    }
}
