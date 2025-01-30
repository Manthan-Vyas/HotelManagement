# Hotel Management System

>**<span style="color:red;">Note: This project is still under development and needs many fixes</span>**


## Overview

This is a Hotel Management System built with Java, Spring Boot, and Maven. It is just a demo project for managing hotel reservations, users, and rooms. The project includes various features such as HATEOAS, ResponseWrapper, internationalization (i18n), security, role-based authorization, custom validations, filters, exception handling, and mappers.

## Key Features

### HATEOAS
Hypermedia as the Engine of Application State (HATEOAS) is implemented to provide navigable links in API responses.

### ResponseWrapper
All API responses are wrapped in a standard format to ensure consistency and ease of parsing.

### Internationalization (i18n)
The project supports multiple languages, allowing for internationalization of messages and labels. For now validation messages for English are supported.

### Security
Spring Security is used to secure the application, including role-based access control.

### Role-Based Auth
Different roles (e.g., Admin, User) have different access levels and permissions within the application.

### Custom Validations
Custom validation annotations are used to enforce business rules and constraints on data.

### Filters
Filters are used to process requests and responses, such as logging and authentication.

### Exception Handling
Global exception handling is implemented to manage and respond to errors consistently.

### Mapper
Custom Mappers are used to map between entities, DTOs, and models, ensuring clean and maintainable code.

## Project Structure

- `src/main/java/com/akm/hotelmanagement` - Main source code
- `src/main/resources` - Configuration files
- `src/test/java/com/akm/hotelmanagement` - Test source code

## How It Works

### Entity -> DTO -> Model Conversions
Data is transferred between different layers of the application using Data Transfer Objects (DTOs) and models. Entities represent the database structure, DTOs are used for data transfer, and models are used for business logic.

### Controller -> Service -> Repository
The application follows a layered architecture, with controllers handling incoming requests, services processing business logic, and repositories interacting with the database.

### Filters
Filters are used to process incoming requests and outgoing responses. For example, logging filters can log request details, while authentication filters can verify user credentials.

### Exception Handling
Global exception handling is implemented to manage and respond to errors consistently. Custom exceptions are used to handle specific error cases.

### Dto(s)
There are separate DTOs for requests and responses, which are used to transfer data between the client and server.

### ResponseWrapper
Tried to wrap all responses in a `ResponseWrapper` class to provide a consistent structure. This includes metadata such as status, message, and data.

```json
{
  "status": true,
  "code": 200,
  "message": "OK",
  "data": {
    "id": "fc7b27ea-9544-4171-aac2-be5ef64046be",
    "name": "Admin",
    "email": "admin@example.com",
    "username": "admin",
    "phone": "1234567890",
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8081/users/admin"
      },
      {
        "rel": "reservations",
        "href": "http://localhost:8081/users/admin/reservations?page=0&size=10&sortBy=reservationDate&sortDir=asc{&filterBy,filterValue}"
      }
    ]
  },
  "error": {},
  "meta": {
    "timestamp": "2025-01-31T02:42:06.739513200",
    "apiVersion": "1.0",
    "requestId": "12",
    "server": "localhost",
    "environment": "dev"
  }
}
```

## Entities and Relationships

### Hotel
- Contains information about the hotel.
- Connected to multiple `Room` entities.

### Amenity
- Represents amenities available in the hotel.
- Can be associated with `Room` entities.

### User
- Represents a user of the system.
- Can have different roles (e.g., Admin, Guest).
- Connected to multiple `Reservation` entities.

### Room
- Represents a room in the hotel.
- Connected to a `Hotel` entity.
- Can have multiple `Amenity` entities.
- Connected to multiple `Reservation` entities.

### Reservation
- Represents a reservation made by a user.
- Connected to a `User` entity.
- Connected to a `Room` entity.

## Actions by Authority

### Admin
- Manage hotels, rooms, and amenities.
- View and manage all reservations.
- Manage users and their roles.

### HotelAdmin
- Manage rooms and amenities.
- View and manage reservations for their hotel.

### User
- View available rooms and amenities.
- Make reservations.
- View and manage their own reservations.

### Guest
- View available rooms and amenities.
- View hotel information.
- Register and login.

## Testing

### Unit Tests
- Added unit tests for mappers, services, filters, and DTOs.
- Working on controller tests to ensure comprehensive coverage.

## Technologies Used

- Java
- Spring Boot
- Maven
- Swagger
- HATEOAS
- Spring Security
- MapStruct

## License

This project is licensed under the MIT License.