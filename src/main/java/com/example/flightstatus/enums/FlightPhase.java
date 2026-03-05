package com.example.flightstatus.enums;

public enum FlightPhase {
    BOARDING(30),
    TAXI_OUT(15),
    TAKEOFF_CLIMB(25),
    CRUISE(210),   // 3.5 hours
    DESCENT(25),
    LANDING(5),
    TAXI_IN(10);

    private final int durationMinutes;

    FlightPhase(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
