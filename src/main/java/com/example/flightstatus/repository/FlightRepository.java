package com.example.flightstatus.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.enums.FlightStatus;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findByStatus(FlightStatus status);

    @Query("SELECT f FROM Flight f WHERE f.status = :status ORDER BY f.startTime DESC")
    List<Flight> findByStatusOrderByStartTimeDesc(@Param("status") FlightStatus status);

    @Query("SELECT f FROM Flight f WHERE f.startTime >= :startTime AND f.startTime <= :endTime ORDER BY f.startTime DESC")
    List<Flight> findFlightsInDateRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.status = :status")
    long countByStatus(@Param("status") FlightStatus status);
}
