# Restaurant Reservation System

A sophisticated restaurant reservation system built with Spring Boot using a **layered architecture** with event-driven communication.

## Overview

This system allows customers to:
- Create new reservations
- View their upcoming reservations
- Update existing reservations
- Cancel reservations
- Receive notifications via Email and/or SMS
- Get automated reminders 4 hours before their reservation

## Architecture

The application follows a **traditional layered architecture** pattern with two main services:

### 1. Reservation Service
Core domain handling all reservation operations:
- **Controller Layer** - REST API endpoints for reservation management
- **Service Layer** - Business logic, validation, and orchestration
- **Repository Layer** - Data persistence using Spring Data JPA
- **Scheduled Tasks** - Automated reminder notifications

### 2. Notification Service
Handles customer notifications:
- **Event Listeners** - Reacts to reservation events (created, updated, cancelled)
- **Template Service** - Builds notification messages from externalized templates
- **Notification Channels** - Supports Email and SMS

### Event-Driven Communication
Services communicate through **Spring Events (pub-sub pattern)**, ensuring:
- Loose coupling between services

### Scheduled Jobs
The system includes automated background tasks:
- **Reminder Scheduler** - Runs every 5 minutes to send reminders for reservations 4 hours away
- Prevents duplicate reminders using `reminderSent` flag
- Only sends reminders for confirmed reservations

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Scheduling** - Automated background tasks
- **Spring Events** - Event-driven communication
- **SpringDoc OpenAPI 3.0** - Interactive API documentation (Swagger UI)
- **H2 Database** - In-memory database
- **Lombok** - Reduce boilerplate code
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Maven** - Build tool

## Project Structure

```
src/main/java/com/umpisa/restaurant/
├── reservationservice/              # Reservation Service
│   ├── controller/                 # REST API controllers
│   │   └── ReservationController.java
│   ├── service/                   # Business logic layer
│   │   ├── ReservationService.java
│   │   ├── ReservationServiceImpl.java
│   │   └── ReservationReminderScheduler.java  # Scheduled reminder job
│   ├── mapper/                    # Entity/DTO mappers
│   │   └── ReservationMapper.java
│   ├── repository/                # Data access layer
│   │   └── ReservationRepository.java
│   └── model/                    # Domain models
│       ├── dto/                  # Request/Response DTOs
│       └── entity/               # Entities, enums, and events
│
├── notificationservice/            # Notification Service
│   ├── service/                   # Notification services
│   │   ├── NotificationService.java
│   │   ├── NotificationTemplateService.java
│   │   └── event/               # Event listeners
│   │       └── ReservationEventListener.java
│   └── model/                    # Notification models
│       ├── NotificationRequest.java
│       └── NotificationTemplateProperties.java  # YAML config binding
│
├── config/                        # Application configuration
│   └── OpenApiConfig.java
│
└── shared/                        # Shared components
    └── exceptions/               # Global exception handling
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

### Postman Collection

For users who prefer Postman for API testing, a pre-configured collection is available:

**📦 Collection File**: `restaurant-reservation-api.postman_collection.json`

**How to use**:
1. Open Postman
2. Click **Import** in the top-left corner
3. Select the `restaurant-reservation-api.postman_collection.json` file from the project root
4. The collection will be imported with all API endpoints pre-configured
5. Make sure the application is running on `http://localhost:8080`
6. Execute requests directly from Postman

The collection includes all endpoints with example payloads for easy testing.

## Key Features

### Business Features
- ✅ **Reservation Management** - Create, view, update, and cancel reservations
- ✅ **Multi-Channel Notifications** - Email, SMS, or both
- ✅ **Automated Reminders** - Scheduled job runs every 5 minutes to send reminders
  - Sends notifications 4 hours before reservation time
  - Prevents duplicate reminders with `reminderSent` flag
  - Only sends to confirmed reservations
- ✅ **Email Filtering** - Query reservations by customer email
- ✅ **Business Validation** - Future dates, guest count, status checks

### Technical Features
- ✅ **RESTful API** - Clean, resource-oriented API design
- ✅ **Event-Driven Architecture** - Decoupled services via Spring Events
- ✅ **Scheduled Jobs** - Spring `@Scheduled` for automated tasks
  - **Cron Expression**: `0 */5 * * * *` (every 5 minutes)
  - **Window Check**: Finds reservations between 4h and 4h5m from now
  - **Error Handling**: Continues processing if one reminder fails
- ✅ **Externalized Templates** - Notification messages in YAML configuration
- ✅ **DTO Pattern** - Separation between API contracts and domain models
- ✅ **Global Exception Handling** - Consistent error responses
- ✅ **Interactive API Docs** - Swagger UI for testing

## Scheduled Jobs Details

### Reservation Reminder Scheduler

**Class**: `ReservationReminderScheduler`
**Schedule**: Every 5 minutes (`0 */5 * * * *`)
**Purpose**: Send reminders 4 hours before reservation time

**How it works**:
1. Runs every 5 minutes automatically
2. Calculates a time window (4 hours to 4 hours 5 minutes from now)
3. Finds confirmed reservations in this window that haven't received reminders
4. Sends notification via customer's preferred channel (Email/SMS)
5. Marks reservation as `reminderSent = true` to prevent duplicates
6. Logs success/failure for each reminder

**Example**:
- Current time: 10:00 AM
- Scheduler finds reservations between 2:00 PM - 2:05 PM
- Sends reminder: "Your reservation is in 4 hours..."
- Next run: 10:05 AM (checks 2:05 PM - 2:10 PM reservations)

## API Endpoints

All endpoints are prefixed with `/api/reservations`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/reservations` | Create a new reservation |
| `GET` | `/api/reservations?email={email}` | Get upcoming reservations for a customer |
| `GET` | `/api/reservations/{id}` | Get reservation by ID |
| `PUT` | `/api/reservations/{id}` | Update an existing reservation |
| `DELETE` | `/api/reservations/{id}` | Cancel a reservation |

## Configuration

### Notification Templates

Templates are externalized in `application.yml` and can be modified without redeployment:

```yaml
notification:
  templates:
    confirmation:
      subject: "Reservation Confirmed - ID #{reservationId}"
      body: |
        Dear {customerName},
        Your reservation has been confirmed!
        ...
    reminder:
      subject: "Reminder: Your Reservation Today at {time}"
      body: |
        Dear {customerName},
        This is a friendly reminder about your upcoming reservation.
        Your table will be ready in approximately 4 hours.
        ...
```

**Placeholders**: `{customerName}`, `{reservationId}`, `{dateTime}`, `{numberOfGuests}`, `{time}`