package com.example.flightstatus.entity;

import java.time.LocalDateTime;

import com.example.flightstatus.enums.FlightPhase;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "metrics")
public class Metric {
    
    public Metric() {
    }
    
    public Metric(String id, Flight flight, LocalDateTime timestamp, int simulatedMinute, FlightPhase phase,
                  int altitude, int airspeed, int heading, Double latitude, Double longitude,
                  int fuelPercentage, Double outsideAirTemperature, int etaMinutes) {
        this.id = id;
        this.flight = flight;
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
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;

    private LocalDateTime timestamp;           // real system time when metric was recorded
    private int simulatedMinute;                // elapsed minutes since start (0..total-1)

    @Enumerated(EnumType.STRING)
    private FlightPhase phase;

    private int altitude;                        // feet
    private int airspeed;                         // knots
    private int heading;                           // degrees (0-359)
    private Double latitude;
    private Double longitude;
    private int fuelPercentage;                    // 0-100
    private Double outsideAirTemperature;           // Celsius (or Fahrenheit, choose one)
    private int etaMinutes;                          // estimated minutes to arrival

    // Explicit getters and setters for Lombok compatibility issues
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getSimulatedMinute() {
        return simulatedMinute;
    }

    public void setSimulatedMinute(int simulatedMinute) {
        this.simulatedMinute = simulatedMinute;
    }

    public FlightPhase getPhase() {
        return phase;
    }

    public void setPhase(FlightPhase phase) {
        this.phase = phase;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getAirspeed() {
        return airspeed;
    }

    public void setAirspeed(int airspeed) {
        this.airspeed = airspeed;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getFuelPercentage() {
        return fuelPercentage;
    }

    public void setFuelPercentage(int fuelPercentage) {
        this.fuelPercentage = fuelPercentage;
    }

    public Double getOutsideAirTemperature() {
        return outsideAirTemperature;
    }

    public void setOutsideAirTemperature(Double outsideAirTemperature) {
        this.outsideAirTemperature = outsideAirTemperature;
    }

    public int getEtaMinutes() {
        return etaMinutes;
    }

    public void setEtaMinutes(int etaMinutes) {
        this.etaMinutes = etaMinutes;
    }

    @Override
    public String toString() {
        return "Metric{" +
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
