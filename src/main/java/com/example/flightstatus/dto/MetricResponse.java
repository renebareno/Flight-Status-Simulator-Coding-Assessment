package com.example.flightstatus.dto;

import com.example.flightstatus.enums.FlightPhase;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MetricResponse {
    private String id;
    private LocalDateTime timestamp;
    private int simulatedMinute;
    private FlightPhase phase;
    private int altitude;
    private int airspeed;
    private int heading;
    private Double latitude;
    private Double longitude;
    private int fuelPercentage;
    private Double outsideAirTemperature;
    private int etaMinutes;

    public MetricResponse() {}

    public MetricResponse(String id, LocalDateTime timestamp, int simulatedMinute, FlightPhase phase,
                         int altitude, int airspeed, int heading, Double latitude, Double longitude,
                         int fuelPercentage, Double outsideAirTemperature, int etaMinutes) {
        this.id = id;
        this.timestamp = timestamp;
        this.simulatedMinute = simulatedMinute;
        this.phase = phase;
        this.altitude = altitude;
        this.airspeed = airspeed;
        this.heading = heading;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fuelPercentage = fuelPercentage;
        this.outsideAirTemperature = outsideAirTemperature;
        this.etaMinutes = etaMinutes;
    }

    // explicit getter used in tests
    public int getSimulatedMinute() {
        return simulatedMinute;
    }
}
