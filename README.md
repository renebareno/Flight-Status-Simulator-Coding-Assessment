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
curl -X POST http://localhost:8080/flights

# List flights
curl http://localhost:8080/flights

# Get flight status
curl http://localhost:8080/flights/{id}

# Get metric history
curl http://localhost:8080/flights/{id}/history
```