package com.example.flightstatus.dto;

import com.example.flightstatus.enums.FlightStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDetail {
    private String id;
    private FlightStatus status;
    private LocalDateTime startTime;
    private MetricResponse latestMetric;

    public FlightDetail() {}

    public FlightDetail(String id, FlightStatus status, LocalDateTime startTime, MetricResponse latestMetric) {
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.latestMetric = latestMetric;
    }

    // explicit getters used in tests
    public String getId() {
        return id;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public MetricResponse getLatestMetric() {
        return latestMetric;
    }
}
