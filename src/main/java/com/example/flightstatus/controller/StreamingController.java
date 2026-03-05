package com.example.flightstatus.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.example.flightstatus.service.StreamingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flights")
@Tag(name = "Flight Streaming", description = "Real-time flight metrics streaming APIs")
public class StreamingController {

    private final StreamingService streamingService;

    public StreamingController(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream flight metrics", description = "Subscribe to real-time metrics stream for a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SSE stream established"),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public SseEmitter streamFlightMetrics(@Parameter(description = "Flight ID") @PathVariable String id) {
        return streamingService.subscribeToFlight(id);
    }
}