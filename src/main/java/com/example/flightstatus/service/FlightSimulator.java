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
import com.example.flightstatus.service.StreamingService;

@Service
public class FlightSimulator {
    private static final Logger logger = LoggerFactory.getLogger(FlightSimulator.class);

    private final SimulationProperties properties;
    private final FlightRepository flightRepository;
    private final MetricRepository metricRepository;
    private final StreamingService streamingService;

    // Hardcoded LAX and JFK coordinates for simplicity
    private static final double LAX_LATITUDE = 33.9425;
    private static final double LAX_LONGITUDE = -118.4081;
    private static final double JFK_LATITUDE = 40.6413;
    private static final double JFK_LONGITUDE = -73.7781;

    public FlightSimulator(SimulationProperties properties, FlightRepository flightRepository,
                          MetricRepository metricRepository, StreamingService streamingService) {
        this.properties = properties;
        this.flightRepository = flightRepository;
        this.metricRepository = metricRepository;
        this.streamingService = streamingService;
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
        streamingService.pushMetric(flight.getId(), initialMetric);

        logger.info("Flight started with ID: {}", flight.getId());
        return flight;
    }

    /**
     * Update simulation for all active flights.
     * Called periodically to check if new metrics should be generated.
     */
    @Scheduled(fixedDelayString = "${simulation.updateIntervalMillis}")
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
                    streamingService.pushMetric(flight.getId(), finalMetric);
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
                streamingService.pushMetric(flight.getId(), newMetric);
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
        int cumulativeMinutes = 0;
        FlightPhase currentPhase = null;
        int phaseStartMinute = 0;
        int phaseDuration = 0;

        // Find which phase we're in
        for (FlightPhase phase : FlightPhase.values()) {
            phaseDuration = phase.getDurationMinutes();
            if (simulatedMinute < cumulativeMinutes + phaseDuration) {
                currentPhase = phase;
                phaseStartMinute = cumulativeMinutes;
                break;
            }
            cumulativeMinutes += phaseDuration;
        }

        if (currentPhase == null) {
            currentPhase = FlightPhase.values()[FlightPhase.values().length - 1];
            phaseStartMinute = cumulativeMinutes;
            phaseDuration = currentPhase.getDurationMinutes();
        }

        int minuteInPhase = simulatedMinute - phaseStartMinute;

        // Delegate to phase-specific method
        return switch (currentPhase) {
            case BOARDING -> new FlightPhaseData(FlightPhase.BOARDING, 0, 0, 270, LAX_LATITUDE, LAX_LONGITUDE, 100, 20.0);
            case TAXI_OUT -> new FlightPhaseData(FlightPhase.TAXI_OUT, 0, 15, 270, LAX_LATITUDE, LAX_LONGITUDE, 99, 20.0);
            case LANDING  -> new FlightPhaseData(FlightPhase.LANDING, 0, 140, 90, JFK_LATITUDE, JFK_LONGITUDE, 5, 15.0);
            case TAXI_IN  -> new FlightPhaseData(FlightPhase.TAXI_IN, 0, 10, 90, JFK_LATITUDE, JFK_LONGITUDE, 5, 15.0);
            case TAKEOFF_CLIMB -> {
                double progress = (double) minuteInPhase / phaseDuration;
                yield new FlightPhaseData(
                    FlightPhase.TAKEOFF_CLIMB,
                    (int) (35000 * progress),
                    (int) (450 * progress),
                    90,
                    LAX_LATITUDE + (JFK_LATITUDE - LAX_LATITUDE) * progress * 0.1,
                    LAX_LONGITUDE + (JFK_LONGITUDE - LAX_LONGITUDE) * progress * 0.1,
                    (int) (100 - 5 * progress),
                    20.0 - 6.5 * progress
                );
            }
            case CRUISE -> {
                double progress = (double) minuteInPhase / phaseDuration;
                yield new FlightPhaseData(
                    FlightPhase.CRUISE,
                    35000,
                    450,
                    90,
                    LAX_LATITUDE + (JFK_LATITUDE - LAX_LATITUDE) * (0.1 + progress * 0.8),
                    LAX_LONGITUDE + (JFK_LONGITUDE - LAX_LONGITUDE) * (0.1 + progress * 0.8),
                    (int) (95 - 80 * progress),
                    -55.0
                );
            }
            case DESCENT -> {
                double progress = (double) minuteInPhase / phaseDuration;
                yield new FlightPhaseData(
                    FlightPhase.DESCENT,
                    (int) (35000 * (1 - progress)),
                    (int) (450 * (1 - progress * 0.8)),
                    90,
                    LAX_LATITUDE + (JFK_LATITUDE - LAX_LATITUDE) * (0.9 + progress * 0.05),
                    LAX_LONGITUDE + (JFK_LONGITUDE - LAX_LONGITUDE) * (0.9 + progress * 0.05),
                    (int) (15 - 10 * progress),
                    -55.0 + 6.5 * progress
                );
            }
        };
    }

    /**
     * Helper record to hold phase-specific data.
     */
    private record FlightPhaseData(FlightPhase phase, int altitude, int airspeed, int heading,
                                   double latitude, double longitude, int fuelPercentage, double temperature) {}
}
