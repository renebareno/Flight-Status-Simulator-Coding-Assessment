package com.example.flightstatus.dto;

import com.example.flightstatus.enums.FlightPhase;

import java.time.LocalDateTime;

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

    // Getters
    public String getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getSimulatedMinute() {
        return simulatedMinute;
    }

    public FlightPhase getPhase() {
        return phase;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getAirspeed() {
        return airspeed;
    }

    public int getHeading() {
        return heading;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getFuelPercentage() {
        return fuelPercentage;
    }

    public Double getOutsideAirTemperature() {
        return outsideAirTemperature;
    }

    public int getEtaMinutes() {
        return etaMinutes;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSimulatedMinute(int simulatedMinute) {
        this.simulatedMinute = simulatedMinute;
    }

    public void setPhase(FlightPhase phase) {
        this.phase = phase;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public void setAirspeed(int airspeed) {
        this.airspeed = airspeed;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setFuelPercentage(int fuelPercentage) {
        this.fuelPercentage = fuelPercentage;
    }

    public void setOutsideAirTemperature(Double outsideAirTemperature) {
        this.outsideAirTemperature = outsideAirTemperature;
    }

    public void setEtaMinutes(int etaMinutes) {
        this.etaMinutes = etaMinutes;
    }

    @Override
    public String toString() {
        return "MetricResponse{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", simulatedMinute=" + simulatedMinute +
                ", phase=" + phase +
                ", altitude=" + altitude +
                ", airspeed=" + airspeed +
                ", heading=" + heading +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", fuelPercentage=" + fuelPercentage +
                ", outsideAirTemperature=" + outsideAirTemperature +
                ", etaMinutes=" + etaMinutes +
                '}';
    }
}
