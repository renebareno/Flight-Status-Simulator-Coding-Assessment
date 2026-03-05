package com.example.flightstatus.streaming;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.flightstatus.entity.Metric;

@Service
public class StreamingService {

    private static final Logger logger = LoggerFactory.getLogger(StreamingService.class);
    private final ConcurrentHashMap<String, List<SseEmitter>> flightEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribeToFlight(String flightId) {
        SseEmitter emitter = new SseEmitter(300000L); // 5 minutes timeout
        flightEmitters.computeIfAbsent(flightId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(flightId, emitter));
        emitter.onTimeout(() -> removeEmitter(flightId, emitter));
        emitter.onError((throwable) -> removeEmitter(flightId, emitter));

        logger.info("Client subscribed to flight {} metrics stream", flightId);
        return emitter;
    }

    public void pushMetric(String flightId, Metric metric) {
        List<SseEmitter> emitters = flightEmitters.get(flightId);
        if (emitters != null) {
            emitters.removeIf(emitter -> {
                try {
                    emitter.send(SseEmitter.event().data(metric));
                    return false; // keep emitter
                } catch (IOException e) {
                    logger.warn("Failed to send metric to emitter for flight {}, removing emitter", flightId);
                    emitter.complete();
                    return true; // remove emitter
                }
            });
        }
    }

    private void removeEmitter(String flightId, SseEmitter emitter) {
        List<SseEmitter> emitters = flightEmitters.get(flightId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                flightEmitters.remove(flightId);
            }
        }
        logger.info("Emitter removed for flight {}", flightId);
    }
}