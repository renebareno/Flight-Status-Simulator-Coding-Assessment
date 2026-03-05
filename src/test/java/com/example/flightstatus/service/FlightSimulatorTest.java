package com.example.flightstatus.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.flightstatus.SimulationProperties;
import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.entity.Metric;
import com.example.flightstatus.enums.FlightPhase;
import com.example.flightstatus.repository.TestFlightRepository;
import com.example.flightstatus.repository.TestMetricRepository;

class FlightSimulatorTest {

    private FlightSimulator simulator;

    @BeforeEach
    void setup() {
        // Create test properties with fast simulation
        SimulationProperties props = new SimulationProperties();
        props.setAccelerationFactor(1);
        props.setTotalDurationMinutes(320);

        // Use test repository implementations
        TestFlightRepository flightRepo = new TestFlightRepository();
        TestMetricRepository metricRepo = new TestMetricRepository();

        simulator = new FlightSimulator(props, flightRepo, metricRepo);
    }

    @Test
    void phaseDeterminationAndEta() throws Exception {
        Flight flight = new Flight();
        flight.setStartTime(LocalDateTime.now());

        // minute 0 should be boarding
        Metric m0 = invokeGenerateMetric(0, flight);
        assertEquals(FlightPhase.BOARDING, m0.getPhase(), "minute 0 phase");
        assertEquals(320 - 0, m0.getEtaMinutes(), "eta at start");

        // minute exactly at boundary to TAXI_OUT
        Metric m30 = invokeGenerateMetric(30, flight);
        assertEquals(FlightPhase.TAXI_OUT, m30.getPhase(), "minute 30 phase");

        // middle of cruise
        Metric m100 = invokeGenerateMetric(100, flight);
        assertEquals(FlightPhase.CRUISE, m100.getPhase(), "minute 100 phase should be cruise");
        assertEquals(320 - 100, m100.getEtaMinutes(), "eta updates correctly");

        // last minute should be TAXI_IN (phase durations sum to 320)
        Metric m319 = invokeGenerateMetric(319, flight);
        assertEquals(FlightPhase.TAXI_IN, m319.getPhase(), "final minute phase");
    }

    @Test
    void calculateElapsedSimulatedMinutes() throws Exception {
        Flight flight = new Flight();
        flight.setStartTime(LocalDateTime.now().minusSeconds(120));

        Method calc = FlightSimulator.class.getDeclaredMethod("calculateElapsedSimulatedMinutes", Flight.class);
        calc.setAccessible(true);
        int elapsed = (int) calc.invoke(simulator, flight);

        // 120 real seconds * 1 factor (test config) = 120 simulated seconds -> 2 minutes
        assertEquals(2, elapsed);
    }

    @Test
    void metricGenerationWithPhaseInterpolation() throws Exception {
        // Test that metrics are generated with correct phase and interpolated values
        Flight flight = new Flight();
        flight.setId("test-flight");
        flight.setStartTime(LocalDateTime.now());

        // Test takeoff phase where altitude changes
        // TAKEOFF_CLIMB starts at minute 45 (BOARDING 0-29, TAXI_OUT 30-44)
        Metric climbMetric = invokeGenerateMetric(50, flight);
        assertNotNull(climbMetric);
        assertEquals(FlightPhase.TAKEOFF_CLIMB, climbMetric.getPhase());
        assertTrue(climbMetric.getAltitude() > 0, "Should climb to altitude");
        assertTrue(climbMetric.getAirspeed() > 0, "Should accelerate");
    }

    private Metric invokeGenerateMetric(int minute, Flight flight) throws Exception {
        Method gm = FlightSimulator.class.getDeclaredMethod("generateMetricForMinute", Flight.class, int.class);
        gm.setAccessible(true);
        return (Metric) gm.invoke(simulator, flight, minute);
    }
}
