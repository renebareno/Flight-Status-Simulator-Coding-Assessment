package com.example.flightstatus.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.enums.FlightStatus;

public class TestFlightRepository implements FlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    @Override
    public List<Flight> findByStatus(FlightStatus status) {
        return new ArrayList<>();
    }

    @Override
    public List<Flight> findByStatusOrderByStartTimeDesc(FlightStatus status) {
        return new ArrayList<>();
    }

    @Override
    public List<Flight> findFlightsInDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return new ArrayList<>();
    }

    @Override
    public long countByStatus(FlightStatus status) {
        return 0;
    }

    @Override
    public Optional<Flight> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Flight> findAllById(Iterable<String> ids) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Flight> S save(S entity) {
        return entity;
    }

    @Override
    public List<Flight> findAll() {
        return new ArrayList<>();
    }

    @Override
    public List<Flight> findAll(Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public Page<Flight> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Flight> List<S> saveAll(Iterable<S> entities) {
        return new ArrayList<>();
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends Flight> S saveAndFlush(S entity) {
        return entity;
    }

    @Override
    public <S extends Flight> List<S> saveAllAndFlush(Iterable<S> entities) {
        return new ArrayList<>();
    }

    @Override
    public void deleteInBatch(Iterable<Flight> entities) {
    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public void deleteAllInBatch(Iterable<Flight> entities) {
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> ids) {
    }

    @Override
    public Flight getOne(String id) {
        return null;
    }

    @Override
    public Flight getById(String id) {
        return null;
    }

    @Override
    public Flight getReferenceById(String id) {
        return null;
    }

    @Override
    public <S extends Flight> List<S> findAll(Example<S> example) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Flight> List<S> findAll(Example<S> example, Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Flight> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Flight> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Flight> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Flight> java.util.Optional<S> findOne(Example<S> example) {
        return java.util.Optional.empty();
    }

    @Override
    public <S extends Flight, R> R findBy(Example<S> example, java.util.function.Function<org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String id) {
    }

    @Override
    public void delete(Flight entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
    }

    @Override
    public void deleteAll(Iterable<? extends Flight> entities) {
    }

    @Override
    public void deleteAll() {
    }
}
