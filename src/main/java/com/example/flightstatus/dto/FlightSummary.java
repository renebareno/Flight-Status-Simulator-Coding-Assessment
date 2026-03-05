package com.example.flightstatus.dto;


import lombok.Data;

import java.time.LocalDateTime;

import com.example.flightstatus.enums.FlightStatus;

@Data
public class FlightSummary {
    private String id;
    private FlightStatus status;
    private LocalDateTime startTime;

    public FlightSummary() {}

    public FlightSummary(String id, FlightStatus status, LocalDateTime startTime) {
        this.id = id;
        this.status = status;
        this.startTime = startTime;
    }

    // explicit getters for tests and external code
    public String getId() {
        return id;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
