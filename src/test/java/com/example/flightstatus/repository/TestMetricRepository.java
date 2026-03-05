package com.example.flightstatus.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.entity.Metric;
import com.example.flightstatus.enums.FlightPhase;

public class TestMetricRepository implements MetricRepository {

    private final List<Metric> metrics = new ArrayList<>();

    @Override
    public Metric findFirstByFlightOrderBySimulatedMinuteDesc(Flight flight) {
        return null;
    }

    @Override
    public List<Metric> findByFlightOrderBySimulatedMinute(Flight flight) {
        return new ArrayList<>();
    }

    @Override
    public List<Metric> findMetricsInMinuteRange(Flight flight, int minMinute, int maxMinute) {
        return new ArrayList<>();
    }

    @Override
    public long countByFlight(Flight flight) {
        return 0;
    }

    @Override
    public Metric findFirstMetricForPhase(Flight flight, FlightPhase phase) {
        return null;
    }

    @Override
    public List<Metric> findByFlightAndPhaseOrderBySimulatedMinute(Flight flight, FlightPhase phase) {
        return new ArrayList<>();
    }

    @Override
    public Optional<Metric> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Metric> findAllById(Iterable<String> ids) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Metric> S save(S entity) {
        return entity;
    }

    @Override
    public List<Metric> findAll() {
        return new ArrayList<>();
    }

    @Override
    public List<Metric> findAll(Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public Page<Metric> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Metric> List<S> saveAll(Iterable<S> entities) {
        return new ArrayList<>();
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends Metric> S saveAndFlush(S entity) {
        return entity;
    }

    @Override
    public <S extends Metric> List<S> saveAllAndFlush(Iterable<S> entities) {
        return new ArrayList<>();
    }

    @Override
    public void deleteInBatch(Iterable<Metric> entities) {
    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public void deleteAllInBatch(Iterable<Metric> entities) {
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> ids) {
    }

    @Override
    public Metric getOne(String id) {
        return null;
    }

    @Override
    public Metric getById(String id) {
        return null;
    }

    @Override
    public Metric getReferenceById(String id) {
        return null;
    }

    @Override
    public <S extends Metric> List<S> findAll(Example<S> example) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Metric> List<S> findAll(Example<S> example, Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Metric> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Metric> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Metric> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Metric> java.util.Optional<S> findOne(Example<S> example) {
        return java.util.Optional.empty();
    }

    @Override
    public <S extends Metric, R> R findBy(Example<S> example, java.util.function.Function<org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
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
    public void delete(Metric entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
    }

    @Override
    public void deleteAll(Iterable<? extends Metric> entities) {
    }

    @Override
    public void deleteAll() {
    }
}
