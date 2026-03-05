package com.example.flightstatus.controller;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.flightstatus.dto.FlightDetail;
import com.example.flightstatus.dto.FlightSummary;
import com.example.flightstatus.dto.MetricResponse;
import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.entity.Metric;
import com.example.flightstatus.exception.FlightNotFoundException;
import com.example.flightstatus.repository.FlightRepository;
import com.example.flightstatus.repository.MetricRepository;
import com.example.flightstatus.service.FlightSimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flights")
@Tag(name = "Flight Management", description = "APIs for managing flight simulations")
public class FlightController {

    private final FlightSimulator flightSimulator;
    private final FlightRepository flightRepository;
    private final MetricRepository metricRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

    public FlightController(FlightSimulator flightSimulator, FlightRepository flightRepository,
            MetricRepository metricRepository, ModelMapper modelMapper) {
        this.flightSimulator = flightSimulator;
        this.flightRepository = flightRepository;
        this.metricRepository = metricRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * POST /flights - Start a new flight
     * Returns 201 Created with location header
     */
    @PostMapping
    @Operation(summary = "Start a new flight simulation", description = "Creates and starts a new flight simulation with real-time metrics generation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Flight created successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlightSummary> startFlight() {
        Flight flight = flightSimulator.startFlight();
        logger.info("flight: {}", flight.toString());
        FlightSummary summary = modelMapper.map(flight, FlightSummary.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(flight.getId())
                .toUri();
        logger.info("Summary: {}", summary.toString());
        return ResponseEntity.created(location).body(summary);
    }

    /**
     * GET /flights - List all flights
     */
    @GetMapping
    @Operation(summary = "Get all flights", description = "Retrieves a list of all flight simulations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of flights retrieved successfully")
    })
    public ResponseEntity<List<FlightSummary>> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        List<FlightSummary> summaries = flights.stream()
                .map(flight -> modelMapper.map(flight, FlightSummary.class))
                .toList();
        return ResponseEntity.ok(summaries);
    }

    /**
     * GET /flights/{id} - Get flight with latest metric
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get flight details", description = "Retrieves detailed information about a specific flight including its latest metric")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<FlightDetail> getFlightDetail(@PathVariable String id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));

        Metric latestMetric = metricRepository.findFirstByFlightOrderBySimulatedMinuteDesc(flight);
        logger.info("latestMetric: {}", latestMetric.toString());

        MetricResponse metricResponse = latestMetric != null
                ? modelMapper.map(latestMetric, MetricResponse.class)
                : null;
        logger.info("latestMetricResponse: {}", metricResponse != null ? metricResponse.toString() : "null");                

        FlightDetail detail = new FlightDetail(
                flight.getId(),
                flight.getStatus(),
                flight.getStartTime(),
                metricResponse);

        return ResponseEntity.ok(detail);
    }

    /**
     * GET /flights/{id}/history - Get all metrics for a flight
     */
    @GetMapping("/{id}/history")
    @Operation(summary = "Get flight history", description = "Retrieves all metrics for a specific flight in chronological order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight history retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<List<MetricResponse>> getFlightHistory(@PathVariable String id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));

        List<Metric> metrics = metricRepository.findByFlightOrderBySimulatedMinute(flight);
        List<MetricResponse> responses = metrics.stream()
                .map(metric -> modelMapper.map(metric, MetricResponse.class))
                .toList();

        return ResponseEntity.ok(responses);
    }
}
