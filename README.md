# Customer Management System

A production-ready REST API built with **Java Spring Boot**, **Gradle**, and **PostgreSQL**. Features full CRUD operations, JUnit test coverage ≥80%, Swagger docs, and Docker support.

## Tech Stack

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## Features

- ✅ Full CRUD REST API for customer management
- ✅ Input validation with meaningful error messages
- ✅ JUnit 5 unit + integration tests (≥80% coverage)
- ✅ Swagger UI at `/swagger-ui.html`
- ✅ Docker + Docker Compose support
- ✅ Environment-based configuration
- ✅ Multi-module Gradle build

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/customers` | Get all customers |
| GET | `/api/v1/customers/{id}` | Get customer by ID |
| GET | `/api/v1/customers/search?query=` | Search by name |
| POST | `/api/v1/customers` | Create customer |
| PUT | `/api/v1/customers/{id}` | Update customer |
| DELETE | `/api/v1/customers/{id}` | Delete customer |

## Running Locally

### Prerequisites
- Java 17+
- Docker & Docker Compose

### With Docker (recommended)
```bash
cp .env.example .env
docker-compose up --build
```
App runs at `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Without Docker
```bash
# Start PostgreSQL locally, then:
./gradlew bootRun
```

## Running Tests
```bash
./gradlew test
# Coverage report: build/reports/jacoco/test/html/index.html
./gradlew jacocoTestCoverageVerification
```

## Project Structure
```
src/
├── main/java/com/kimberly/customermanagement/
│   ├── controller/     # REST controllers
│   ├── service/        # Business logic
│   ├── repository/     # JPA repositories
│   ├── model/          # JPA entities
│   ├── dto/            # Request/Response DTOs
│   ├── exception/      # Custom exceptions + global handler
│   └── config/         # OpenAPI/Swagger config
└── test/               # JUnit 5 unit + integration tests
```

---

Built by [Kimberly Githinji](https://www.kimberlygithinji.site) 🇰🇪
