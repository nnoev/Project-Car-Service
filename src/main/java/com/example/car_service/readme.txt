# Car Service Record Microservice

REST microservice responsible for storing and managing vehicle service records for the [Car Service Management System](https://github.com/nnoev/Project-Car-Service).

The microservice is an independent Spring Boot application. The main MVC application communicates with it through a Spring Cloud OpenFeign client.

## Technologies

- Java 17+
- Spring Boot 4.1.0
- Spring Web
- Spring Data JPA
- Jakarta Bean Validation
- MySQL
- Maven
- JUnit and Mockito
- Lombok

## Architecture

The complete system consists of two independent applications:

- [Main MVC application](https://github.com/nnoev/Project-Car-Service) — port `8080`
- Service Record REST microservice — port `8081`

The microservice uses its own MySQL database named `car_service_record`.

## Functionality

The API supports:

- Creating service records
- Retrieving all service records
- Retrieving records belonging to a user
- Retrieving an individual record
- Updating service records
- Deleting individual records
- Deleting records by user or vehicle
- Calculating the total service cost
- Returning the total record count

## REST API

Base URL:

```text
http://localhost:8081/api/v1/service-records
```

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | Get all service records |
| `GET` | `/{recordId}?userId={userId}` | Get a record belonging to a user |
| `GET` | `/users/{userId}` | Get all records for a user |
| `GET` | `/count` | Get the number of records |
| `GET` | `/total-cost` | Get the total service cost |
| `POST` | `/` | Create a service record |
| `PUT` | `/{recordId}?userId={userId}` | Update a record belonging to a user |
| `DELETE` | `/{recordId}?userId={userId}` | Delete an individual record |
| `DELETE` | `/users/{userId}` | Delete all records for a user |
| `DELETE` | `/vehicles/{vehicleId}` | Delete all records for a vehicle |

## Prerequisites

Install:

- Java 17 or newer
- MySQL
- Git

The repository includes Maven Wrapper, so a separate Maven installation is not required.

## Configuration

The application runs with:

```properties
server.address=127.0.0.1
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/car_service_record?createDatabaseIfNotExist=true
```

Set the database credentials through environment variables:

```text
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

Do not commit real passwords to the repository.

## Running the application

Clone the repository:

```bash
git clone https://github.com/nnoev/Car-Service-Microservice.git
cd Car-Service-Microservice
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8081/api/v1/service-records
```

Start this microservice before using functionality in the main application that depends on service records.

## Running the tests

On Windows:

```powershell
.\mvnw.cmd test
```

On Linux or macOS:

```bash
./mvnw test
```