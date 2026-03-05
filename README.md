# Flight Status Simulator

Spring Boot application to simulate flight status and metrics.

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