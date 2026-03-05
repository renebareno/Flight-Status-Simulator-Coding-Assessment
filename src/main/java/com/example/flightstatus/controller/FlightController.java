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

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightSimulator flightSimulator;
    private final FlightRepository flightRepository;
    private final MetricRepository metricRepository;
    private final ModelMapper modelMapper;

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
    public ResponseEntity<FlightSummary> startFlight() {
        Flight flight = flightSimulator.startFlight();
        FlightSummary summary = modelMapper.map(flight, FlightSummary.class);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(flight.getId())
            .toUri();

        return ResponseEntity.created(location).body(summary);
    }

    /**
     * GET /flights - List all flights
     */
    @GetMapping
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
    public ResponseEntity<FlightDetail> getFlightDetail(@PathVariable String id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));

        Metric latestMetric = metricRepository.findFirstByFlightOrderBySimulatedMinuteDesc(flight);
        MetricResponse metricResponse = latestMetric != null
            ? modelMapper.map(latestMetric, MetricResponse.class)
            : null;

        FlightDetail detail = new FlightDetail(
            flight.getId(),
            flight.getStatus(),
            flight.getStartTime(),
            metricResponse
        );

        return ResponseEntity.ok(detail);
    }

    /**
     * GET /flights/{id}/history - Get all metrics for a flight
     */
    @GetMapping("/{id}/history")
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
