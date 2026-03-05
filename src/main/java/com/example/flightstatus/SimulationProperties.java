package com.example.flightstatus;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simulation")
public class SimulationProperties {
    private int accelerationFactor = 60;   // 1 real second = 60 simulated seconds (1 minute)
    private int totalDurationMinutes = 320; // sum of phases
    private long updateIntervalMillis = 1000; // interval for updating active flights (1 second)

    public int getAccelerationFactor() {
        return accelerationFactor;
    }

    public void setAccelerationFactor(int accelerationFactor) {
        this.accelerationFactor = accelerationFactor;
    }

    public int getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(int totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public long getUpdateIntervalMillis() {
        return updateIntervalMillis;
    }

    public void setUpdateIntervalMillis(long updateIntervalMillis) {
        this.updateIntervalMillis = updateIntervalMillis;
    }
}
