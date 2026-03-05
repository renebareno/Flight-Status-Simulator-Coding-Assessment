# Flight Status Simulator

Spring Boot application to simulate flight status and metrics for a commercial flight from LAX to JFK, exposing real-time flight metrics via a REST API.

## Prerequisites

- Java 21
- Maven
- Docker (optional)

## Build & Run

### Using Maven

```bash
mvn spring-boot:run
```

### Using Docker

1. Build the Docker image:

```bash
docker build -t flight-status-simulator .
```

2. Run the container:

```bash
docker run -p 8080:8080 flight-status-simulator
```

The application will be available at http://localhost:8080.

## API Endpoints

Use the following curl examples to interact with the service:

```bash
# Start flight
curl -X POST http://localhost:8080/v1/flights

# List flights
curl http://localhost:8080/v1/flights

# Get flight status
curl http://localhost:8080/v1/flights/{id}

# Get metric history
curl http://localhost:8080/v1/flights/{id}/history

# Socket - Stream real-time metrics (SSE) (Pick a fresh id !! )
curl -H "Accept: text/event-stream" http://localhost:8080/v1/flights/{id}/stream
```


## Example End-to-End Scenario

### 1. Client calls: POST /v1/flights
   → Flight "abc123" created, simulation starts

### 2. Background thread generates metrics every ~1 sec (1 simulated minute):
   - Minute 0: BOARDING, altitude=0, speed=0
   - Minute 30: TAXI_OUT, climbing
   - Minute 50: TAKEOFF_CLIMB, rapid altitude gain
   - Minute 100: CRUISE, stable 35k ft
   - Minute 250: DESCENT, losing altitude
   - Minute 310: LANDING, on runway
   - Minute 320: TAXI_IN, at gate ✓

### 3. Client connects: GET /v1/flights/abc123/stream
   → Receives SSE events in real-time as metrics are generated

### 4. Client queries: GET /v1/flights/abc123/history
   → Receives all 320+ metric records for analysis

### 5. Flight completes after 320 simulated minutes (4-7 real seconds)
   → Flight completes after 320 simulated minutes

## API Documentation

The application includes Swagger UI for interactive API documentation.

Access the Swagger UI at: http://localhost:8080/swagger-ui/index.html

Or view the OpenAPI specification at: http://localhost:8080/v3/api-docs

## Assumptions:

Acceleration factor = 60 (1 real sec = 1 simulated minute)

Metrics generated every simulated minute

Phase durations as given

LAX coordinates: (33.9416° N, 118.4085° W); JFK: (40.6413° N, 73.7781° W)

Temperature, fuel, etc. are simplified linear approximations.

### Design Decisions:

Used UUID for IDs to avoid collisions.

Scheduled task runs every real second to update active flights.

Stored simulatedMinute to easily detect duplicates and compute ETA.
