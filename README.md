# 🚗 Car Service Management System

A full-stack Spring Boot web application for managing vehicles, service history, and maintenance reminders.

The application allows registered users to manage multiple vehicles, keep track of performed services, and schedule future maintenance through an intuitive web interface built with Thymeleaf.

---

## Features

- User registration and login
- Secure authentication with Spring Security
- Vehicle management
    - Add vehicle
    - Edit vehicle
    - Delete vehicle
    - View vehicle information
- Service record management
    - Add service records
    - Update service history
    - Delete records
- Maintenance reminders
- User profile page
- Session-based authentication
- Responsive user interface using HTML and CSS

---

## Technologies

### Backend

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

### Frontend

- Thymeleaf
- HTML5
- CSS3

### Database

- MySQL

### Build Tool

- Maven

---

## Project Structure

```
src
├── controller
├── service
├── repository
├── model
├── security
├── config
├── templates
└── static
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL

### Installation

1. Clone the repository

```bash
git clone https://github.com/yourusername/Car-Service.git
```

2. Navigate to the project

```bash
cd Car-Service
```

3. Configure your database credentials in

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/car_service
spring.datasource.username=root
spring.datasource.password=your_password
```

4. Run the application

```bash
mvn spring-boot:run
```

or run the main application class directly from your IDE.

---

## Future Improvements

- Email reminders
- File upload for invoices
- Vehicle images
- REST API
- Docker support
- Unit and integration tests
- Pagination and search
- Role-based authorization (Admin/User)

---

## Author

**Nikolay Noev**

GitHub: https://github.com/nnoev