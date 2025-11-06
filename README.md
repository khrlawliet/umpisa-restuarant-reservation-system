# Restaurant Reservation System

A sophisticated restaurant reservation system built with Spring Boot using a **layered architecture** with event-driven communication.

## Overview

This system allows customers to:
- Create new reservations
- View their upcoming reservations
- Update existing reservations
- Cancel reservations
- Receive notifications via Email and/or SMS

## Architecture

The application follows a **traditional layered architecture** pattern with two main services:

1. **Reservation Service** - Core domain handling all reservation operations (Controller → Service → Repository → Model)
2. **Notification Service** - Handles sending notifications to customers

Services communicate through **Spring Events (pub-sub pattern)**, ensuring loose coupling and maintainability.

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **SpringDoc OpenAPI 3.0** - Interactive API documentation (Swagger UI)
- **H2 Database** - In-memory database
- **Lombok** - Reduce boilerplate code
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Maven** - Build tool

## Project Structure

```
src/main/java/com/umpisa/restaurant/
├── reservationservice/         # Reservation Service
│   ├── controller/            # REST API controllers
│   ├── service/              # Business logic layer
│   ├── repository/           # Data access layer
│   └── model/               # DTOs, entities, enums, events
├── notificationservice/       # Notification Service
│   └── service/             # Notification services and event listeners
├── config/                   # Application configuration
│   └── OpenApiConfig.java   # Swagger/OpenAPI configuration
└── shared/                  # Shared components
    └── exceptions/         # Common exceptions and error handling
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Building the Project

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package the application
mvn clean package
```

## Running the Application

```bash
# Run with Maven
mvn spring-boot:run

# Or run the JAR file
java -jar target/restaurant-reservation-system-1.0.0.jar
```

The application will start on `http://localhost:8080`

## H2 Console

The H2 database console is available at: `http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:restaurantdb`
- **Username**: `sa`
- **Password**: (leave empty)

## 📚 API Documentation

### Interactive Swagger UI

The **primary API documentation** is available through Swagger UI:

**🚀 Swagger UI**: http://localhost:8080/swagger-ui.html

Features:
- ✅ **Interactive API Testing** - Try out endpoints directly from your browser
- ✅ **Request/Response Examples** - See example payloads for all endpoints
- ✅ **Schema Documentation** - Detailed information about all data models
- ✅ **Validation Rules** - View all validation constraints inline
- ✅ **Error Responses** - See all possible error codes and formats

For complete documentation including integration with Postman and more examples, see [API.md](API.md).

## API Quick Reference

### Create Reservation
```bash
POST /api/reservations
Content-Type: application/json

{
  "customerName": "John Doe",
  "phoneNumber": "+1234567890",
  "email": "john@example.com",
  "reservationDateTime": "2025-11-10T19:00:00",
  "numberOfGuests": 4,
  "notificationChannel": "BOTH"
}
```

### Get Upcoming Reservations
```bash
GET /api/reservations?email=john@example.com
```

### Get Reservation by ID
```bash
GET /api/reservations/{id}
```

### Update Reservation
```bash
PUT /api/reservations/{id}
Content-Type: application/json

{
  "reservationDateTime": "2025-11-11T20:00:00",
  "numberOfGuests": 6
}
```

### Cancel Reservation
```bash
DELETE /api/reservations/{id}
```

## Notification Channels

- `EMAIL` - Send notifications via email only
- `SMS` - Send notifications via SMS only
- `BOTH` - Send notifications via both email and SMS

**Note**: Current implementation uses mock notification services that log to console.

## Testing

The project includes comprehensive unit tests:

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

**Test Coverage**: 22 tests covering:
- Service layer business logic
- REST controller endpoints
- Event listeners
- Error handling scenarios

## Sample Usage

### Example 1: Create a Reservation

```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Alice Smith",
    "phoneNumber": "+1234567890",
    "email": "alice@example.com",
    "reservationDateTime": "2025-11-15T18:30:00",
    "numberOfGuests": 2,
    "notificationChannel": "EMAIL"
  }'
```

**Response**:
```json
{
  "id": 1,
  "customerName": "Alice Smith",
  "phoneNumber": "+1234567890",
  "email": "alice@example.com",
  "reservationDateTime": "2025-11-15T18:30:00",
  "numberOfGuests": 2,
  "status": "CONFIRMED",
  "notificationChannel": "EMAIL",
  "createdAt": "2025-11-06T10:00:00",
  "updatedAt": "2025-11-06T10:00:00"
}
```

### Example 2: View Upcoming Reservations

```bash
curl http://localhost:8080/api/reservations?email=alice@example.com
```

### Example 3: Update Reservation

```bash
curl -X PUT http://localhost:8080/api/reservations/1 \
  -H "Content-Type: application/json" \
  -d '{
    "reservationDateTime": "2025-11-15T19:00:00",
    "numberOfGuests": 4
  }'
```

### Example 4: Cancel Reservation

```bash
curl -X DELETE http://localhost:8080/api/reservations/1
```

## Validation Rules

- **Customer Name**: 2-100 characters
- **Phone Number**: Valid format (10-15 digits)
- **Email**: Valid email format
- **Reservation Date/Time**: Must be in the future
- **Number of Guests**: 1-50 guests

## Error Handling

The API returns consistent error responses:

```json
{
  "timestamp": "2025-11-06T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Reservation with ID 123 not found",
  "path": "/api/reservations/123"
}
```

## Documentation

- **[Swagger UI](http://localhost:8080/swagger-ui.html)** - Interactive API documentation (Primary)
- [API.md](API.md) - Swagger setup guide and cURL examples
- [DESIGN_DECISIONS.md](DESIGN_DECISIONS.md) - Architecture and design choices

## Future Enhancements

- Implement actual SMS/Email service integration
- Add reservation reminder scheduler (4 hours before)
- Add table management system
- Implement authentication and authorization
- Add reservation conflict detection
- Add rate limiting
- Create Postman collection

## License

This project is developed for Umpisa Technical Assessment.

## Contact

For questions or issues, please contact the development team.
