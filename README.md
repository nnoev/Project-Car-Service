# Car Service Management System

A Spring Boot MVC application for managing vehicles, maintenance reminders, and vehicle service history.

The project consists of two independent Spring Boot applications. This repository contains the main MVC application. Service records are managed by a separate [REST microservice](https://github.com/nnoev/Car-Service-Microservice) and accessed through Spring Cloud OpenFeign.

## System architecture

| Application | Responsibility | Port | Repository |
|---|---|---:|---|
| Main application | Web UI, users, vehicles, reminders and security | `8080` | [Project-Car-Service](https://github.com/nnoev/Project-Car-Service) |
| REST microservice | Service record management | `8081` | [Car-Service-Microservice](https://github.com/nnoev/Car-Service-Microservice) |

The applications run independently and use separate MySQL databases:

- Main application: `car_maintenance`
- REST microservice: `car_service_record`

## Features

### User functionality

- User registration and authentication
- Guest access
- View and edit personal profile
- Change password
- Add, edit and delete vehicles
- Add, edit and delete service records
- Add and delete maintenance reminders
- Mark reminders as completed or pending
- View service history and total service cost

### Administration

Administrators can:

- View the administration dashboard
- View all users, vehicles, reminders and service records
- Change user roles
- Delete users and their related data
- Delete vehicles, reminders and service records
- View cached application statistics

### Additional functionality

- Spring Security authentication and role-based authorization
- CSRF protection
- DTO and entity validation
- Global exception handling
- Scheduled overdue-reminder processing using a cron expression
- Scheduled user-class updates using a fixed delay
- Spring caching for administration statistics
- Application logging
- Responsive Thymeleaf interface

## Technologies

### Backend

- Java 17+
- Spring Boot 3.5.15
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- Spring Cloud OpenFeign
- Spring Validation
- Spring Cache
- Spring Scheduling
- Spring Boot Actuator

### Frontend

- Thymeleaf
- HTML5
- CSS3

### Database and build

- MySQL
- Maven
- Maven Wrapper

### Testing

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- Spring Security Test

## Prerequisites

Install:

- Java 17 or newer
- MySQL
- Git

A separate Maven installation is not required because Maven Wrapper is included.

## Configuration

The main application runs on:

```text
http://localhost:8080
```

Its MySQL connection is:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/car_maintenance?createDatabaseIfNotExist=true
```

Configure sensitive values using environment variables:

The application expects the REST microservice at:

```text
http://localhost:8081
```

## Running the complete system

### 1. Start MySQL

Ensure the MySQL server is running and the configured user can create or access both databases.

### 2. Start the REST microservice

Clone and open the microservice:

```bash
git clone https://github.com/nnoev/Car-Service-Microservice.git
cd Car-Service-Microservice
```

On Windows:

```powershell
.\mvnw.cmd spring-vnw.cmd spring-boot:run
```

The REST API starts on port `8081`.

### 3. Start the main application

Clone this repository:

```bash
git clone https://github.com/nnoev/Project-Car-Service.git
cd Project-Car-Service
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8080
```

The microservice should be started before using service-record functionality.

## Running tests

On Windows:

```powershell
.\mvnw.cmd test
```

On Linux or macOS:

```bash
./mvnw test
```

Tests should use a separate test database and must not modify production or development data.

## Main web pages

The application provides dynamic pages for:

- Home
- Login
- Registration
- User dashboard
- Profile
- Vehicles
- Vehicle form
- Service records
- Service record form
- Maintenance reminders
- Reminder form
- Administration dashboard
- Administration users
- Administration vehicles
- Administration reminders
- Administration service records
- Error handling

## Repository links

- [Main application](https://github.com/nnoev/Project-Car-Service)
- [REST microservice](https://github.com/nnoev/Car-Service-Microservice)
- [Author GitHub profile](https://github.com/nnoev)

## Author

Nikolay Noev