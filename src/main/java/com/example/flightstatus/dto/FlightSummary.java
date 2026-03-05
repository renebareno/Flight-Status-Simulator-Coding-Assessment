package com.example.flightstatus.dto;


import java.time.LocalDateTime;

import com.example.flightstatus.enums.FlightStatus;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "FlightSummary{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                '}';
    }
}
