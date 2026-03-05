package com.example.flightstatus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.entity.Metric;
import com.example.flightstatus.enums.FlightPhase;

@Repository
public interface MetricRepository extends JpaRepository<Metric, String> {
    /**
     * Find the latest metric for a flight (highest simulatedMinute).
     */
    Metric findFirstByFlightOrderBySimulatedMinuteDesc(Flight flight);

    /**
     * Find all metrics for a given flight, ordered by simulated minute.
     */
    List<Metric> findByFlightOrderBySimulatedMinute(Flight flight);

    /**
     * Find metrics for a flight within a simulated minute range.
     */
    @Query("SELECT m FROM Metric m WHERE m.flight = :flight AND m.simulatedMinute >= :minMinute AND m.simulatedMinute <= :maxMinute ORDER BY m.simulatedMinute")
    List<Metric> findMetricsInMinuteRange(@Param("flight") Flight flight, @Param("minMinute") int minMinute, @Param("maxMinute") int maxMinute);

    /**
     * Count total metrics for a flight.
     */
    long countByFlight(Flight flight);

    /**
     * Find the metric at the start of a specific flight phase.
     */
    @Query("SELECT m FROM Metric m WHERE m.flight = :flight AND m.phase = :phase ORDER BY m.simulatedMinute LIMIT 1")
    Metric findFirstMetricForPhase(@Param("flight") Flight flight, @Param("phase") FlightPhase phase);

    /**
     * Find all metrics for a flight at a given phase.
     */
    List<Metric> findByFlightAndPhaseOrderBySimulatedMinute(Flight flight, FlightPhase phase);
}
