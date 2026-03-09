# ConfigService-Dashboard

## Purpose
Dashboard and administration GUI for ConfigService. Provides a web interface to view and manage client applications, their configurations, environments, and logs within the Cantara configuration management ecosystem.

## Tech Stack
- Language: Java 8+
- Framework: Jersey 2.x, Spring 5.x, Jetty 9.x
- Build: Maven
- Key dependencies: ConfigService-SDK, Jackson, AWS SDK, Apache HttpClient

## Architecture
Standalone web application that connects to a ConfigService instance via the ConfigService-SDK. Presents a dashboard UI showing clients overview, configuration details, environment status, application status, and log viewing. Deployed as an executable JAR with embedded Jetty.

## Key Entry Points
- Main application JAR entry point
- Web dashboard at `http://localhost:8087/dashboard/`
- ConfigService-SDK dependency for backend communication

## Development
```bash
# Build
mvn clean install

# Run
java -jar target/ConfigService-Dashboard-*.jar

# Access dashboard
open http://localhost:8087/dashboard/
```

## Domain Context
Operations and administration for the Cantara configuration management system. Companion UI to ConfigService, providing visibility into deployed applications, their configurations, and runtime status.
