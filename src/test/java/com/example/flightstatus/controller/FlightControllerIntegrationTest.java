package com.example.flightstatus.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.flightstatus.dto.FlightDetail;
import com.example.flightstatus.dto.FlightSummary;
import com.example.flightstatus.dto.MetricResponse;
import com.example.flightstatus.entity.Flight;
import com.example.flightstatus.repository.FlightRepository;
import com.example.flightstatus.repository.MetricRepository;
import com.example.flightstatus.service.FlightSimulator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FlightControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Verify Spring context loads properly
        assertThat(mockMvc).isNotNull();
    }
}
