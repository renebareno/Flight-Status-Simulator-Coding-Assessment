package com.example.flightstatus.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.flightstatus.enums.*;

@Entity
@Table(name = "flights")
public class Flight {
    
    public Flight() {
    }
    
    public Flight(String id, LocalDateTime startTime, FlightStatus status, List<Metric> metrics) {
        this.id = id;
        this.startTime = startTime;
        this.status = status;
        this.metrics = metrics;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime startTime;          // real system time when flight started

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    // Optionally cache current phase/metric to avoid querying every time,
    // but we can also derive from latest metric.
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Metric.class)
    private List<Metric> metrics = new ArrayList<>();

    // explicit getters/setters kept for stable compilation with Lombok on Java 21
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", status=" + status +
                '}';
    }

}
