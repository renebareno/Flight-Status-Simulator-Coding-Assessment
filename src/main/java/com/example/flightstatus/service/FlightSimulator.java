package com.example.flightstatus.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flightstatus.SimulationProperties;
import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.entity.Metric;
import com.example.flightstatus.enums.FlightPhase;
import com.example.flightstatus.enums.FlightStatus;
import com.example.flightstatus.repository.FlightRepository;
import com.example.flightstatus.repository.MetricRepository;

@Service
public class FlightSimulator {
    private static final Logger logger = LoggerFactory.getLogger(FlightSimulator.class);

    private final SimulationProperties properties;
    private final FlightRepository flightRepository;
    private final MetricRepository metricRepository;

    // Hardcoded LAX and JFK coordinates for simplicity
    private static final double LAX_LATITUDE = 33.9425;
    private static final double LAX_LONGITUDE = -118.4081;
    private static final double JFK_LATITUDE = 40.6413;
    private static final double JFK_LONGITUDE = -73.7781;

    public FlightSimulator(SimulationProperties properties, FlightRepository flightRepository,
                          MetricRepository metricRepository) {
        this.properties = properties;
        this.flightRepository = flightRepository;
        this.metricRepository = metricRepository;
    }

    /**
     * Start a new flight and generate the first metric (minute 0).
     */
    public Flight startFlight() {
        Flight flight = new Flight();
        flight.setStartTime(LocalDateTime.now());
        flight.setStatus(FlightStatus.ACTIVE);
        flight = flightRepository.save(flight);

        // Generate initial metric at minute 0
        Metric initialMetric = generateMetricForMinute(flight, 0);
        metricRepository.save(initialMetric);

        logger.info("Flight started with ID: {}", flight.getId());
        return flight;
    }

    /**
     * Update simulation for all active flights.
     * Called periodically (every second) to check if new metrics should be generated.
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void updateActiveFlights() {
        var activeFlights = flightRepository.findByStatus(FlightStatus.ACTIVE);
        for (Flight flight : activeFlights) {
            updateFlight(flight);
        }
    }

    /**
     * Update a single flight: compute elapsed time, generate new metric if needed, complete if done.
     */
    @Transactional
    private void updateFlight(Flight flight) {
        int elapsedMinutes = calculateElapsedSimulatedMinutes(flight);
        int totalDuration = properties.getTotalDurationMinutes();

        if (elapsedMinutes >= totalDuration) {
            // Flight completed
            if (!flight.getStatus().equals(FlightStatus.COMPLETED)) {
                flight.setStatus(FlightStatus.COMPLETED);
                flightRepository.save(flight);

                // Generate final metric at totalDuration if not already done
                var lastMetric = metricRepository.findFirstByFlightOrderBySimulatedMinuteDesc(flight);
                if (lastMetric == null || lastMetric.getSimulatedMinute() < totalDuration - 1) {
                    Metric finalMetric = generateMetricForMinute(flight, totalDuration - 1);
                    metricRepository.save(finalMetric);
                }
                logger.info("Flight {} completed", flight.getId());
            }
        } else {
            // Check if we should generate a new metric
            var lastMetric = metricRepository.findFirstByFlightOrderBySimulatedMinuteDesc(flight);
            int lastRecordedMinute = (lastMetric != null) ? lastMetric.getSimulatedMinute() : -1;

            if (elapsedMinutes > lastRecordedMinute) {
                Metric newMetric = generateMetricForMinute(flight, elapsedMinutes);
                metricRepository.save(newMetric);
            }
        }
    }

    /**
     * Calculate elapsed simulated minutes from flight start time.
     * Formula: (current time - start time) * acceleration factor / 60
     */
    private int calculateElapsedSimulatedMinutes(Flight flight) {
        LocalDateTime now = LocalDateTime.now();
        long elapsedRealSeconds = java.time.temporal.ChronoUnit.SECONDS.between(flight.getStartTime(), now);
        int elapsedSimulatedSeconds = (int) (elapsedRealSeconds * properties.getAccelerationFactor());
        return elapsedSimulatedSeconds / 60; // convert to minutes
    }

    /**
     * Generate a metric for a given simulated minute.
     */
    private Metric generateMetricForMinute(Flight flight, int simulatedMinute) {
        Metric metric = new Metric();
        metric.setFlight(flight);
        metric.setTimestamp(LocalDateTime.now());
        metric.setSimulatedMinute(simulatedMinute);

        // Determine current phase and populate metric values
        FlightPhaseData phaseData = determinePhaseAndInterpolate(simulatedMinute);
        metric.setPhase(phaseData.phase);
        metric.setAltitude(phaseData.altitude);
        metric.setAirspeed(phaseData.airspeed);
        metric.setHeading(phaseData.heading);
        metric.setLatitude(phaseData.latitude);
        metric.setLongitude(phaseData.longitude);
        metric.setFuelPercentage(phaseData.fuelPercentage);
        metric.setOutsideAirTemperature(phaseData.temperature);
        metric.setEtaMinutes(properties.getTotalDurationMinutes() - simulatedMinute);

        return metric;
    }

    /**
     * Determine current phase and interpolate realistic values for the given minute.
     */
    private FlightPhaseData determinePhaseAndInterpolate(int simulatedMinute) {
        FlightPhaseData data = new FlightPhaseData();
        int cumulativeMinutes = 0;
        FlightPhase[] phases = FlightPhase.values();
        FlightPhase currentPhase = null;
        int phaseStartMinute = 0;
        int phaseEndMinute = 0;

        // Find which phase we're in
        for (FlightPhase phase : phases) {
            int phaseDuration = phase.getDurationMinutes();
            phaseEndMinute = cumulativeMinutes + phaseDuration;

            if (simulatedMinute < phaseEndMinute) {
                currentPhase = phase;
                phaseStartMinute = cumulativeMinutes;
                break;
            }
            cumulativeMinutes = phaseEndMinute;
        }

        if (currentPhase == null) {
            currentPhase = phases[phases.length - 1];
            phaseStartMinute = cumulativeMinutes;
            phaseEndMinute = cumulativeMinutes + currentPhase.getDurationMinutes();
        }

        data.phase = currentPhase;
        int minuteInPhase = simulatedMinute - phaseStartMinute;
        int phaseDuration = currentPhase.getDurationMinutes();

        // Populate phase-specific values
        switch (currentPhase) {
            case BOARDING:
                data.altitude = 0;
                data.airspeed = 0;
                data.heading = 270; // facing west (runway direction, simplified)
                data.latitude = LAX_LATITUDE;
                data.longitude = LAX_LONGITUDE;
                data.fuelPercentage = 100;
                data.temperature = 20.0;
                break;

            case TAXI_OUT:
                data.altitude = 0;
                data.airspeed = 15;
                data.heading = 270;
                data.latitude = LAX_LATITUDE;
                data.longitude = LAX_LONGITUDE;
                data.fuelPercentage = 99;
                data.temperature = 20.0;
                break;

            case TAKEOFF_CLIMB:
                double climbProgress = (double) minuteInPhase / phaseDuration;
                data.altitude = (int) (35000 * climbProgress); // climb to 35,000 ft
                data.airspeed = (int) (450 * climbProgress);   // accelerate to 450 knots
                data.heading = 90; // heading east after takeoff
                // Interpolate position from LAX towards JFK
                data.latitude = LAX_LATITUDE + (JFK_LATITUDE - LAX_LATITUDE) * climbProgress * 0.1;
                data.longitude = LAX_LONGITUDE + (JFK_LONGITUDE - LAX_LONGITUDE) * climbProgress * 0.1;
                data.fuelPercentage = (int) (100 - 5 * climbProgress);
                data.temperature = 20.0 - 6.5 * climbProgress;
                break;

            case CRUISE:
                double cruiseProgress = (double) minuteInPhase / phaseDuration;
                data.altitude = 35000;
                data.airspeed = 450;
                data.heading = 90;
                // Cruise across country
                data.latitude = LAX_LATITUDE + (JFK_LATITUDE - LAX_LATITUDE) * (0.1 + cruiseProgress * 0.8);
                data.longitude = LAX_LONGITUDE + (JFK_LONGITUDE - LAX_LONGITUDE) * (0.1 + cruiseProgress * 0.8);
                data.fuelPercentage = (int) (95 - 80 * cruiseProgress); // fuel decreases from 95% to ~15%
                data.temperature = -55.0;
                break;

            case DESCENT:
                double descentProgress = (double) minuteInPhase / phaseDuration;
                data.altitude = (int) (35000 * (1 - descentProgress));
                data.airspeed = (int) (450 * (1 - descentProgress * 0.8));
                data.heading = 90;
                // Continue towards JFK
                data.latitude = LAX_LATITUDE + (JFK_LATITUDE - LAX_LATITUDE) * (0.9 + descentProgress * 0.05);
                data.longitude = LAX_LONGITUDE + (JFK_LONGITUDE - LAX_LONGITUDE) * (0.9 + descentProgress * 0.05);
                data.fuelPercentage = (int) (15 - 10 * descentProgress);
                data.temperature = -55.0 + 6.5 * descentProgress;
                break;

            case LANDING:
                data.altitude = 0;
                data.airspeed = 140;
                data.heading = 90;
                data.latitude = JFK_LATITUDE;
                data.longitude = JFK_LONGITUDE;
                data.fuelPercentage = 5;
                data.temperature = 15.0;
                break;

            case TAXI_IN:
                data.altitude = 0;
                data.airspeed = 10;
                data.heading = 90;
                data.latitude = JFK_LATITUDE;
                data.longitude = JFK_LONGITUDE;
                data.fuelPercentage = 5;
                data.temperature = 15.0;
                break;
        }

        return data;
    }

    /**
     * Helper class to hold phase-specific data.
     */
    private static class FlightPhaseData {
        FlightPhase phase;
        int altitude;
        int airspeed;
        int heading;
        double latitude;
        double longitude;
        int fuelPercentage;
        double temperature;
    }
}
