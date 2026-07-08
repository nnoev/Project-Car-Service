Car Service Management System

A Spring MVC web application that allows users to manage their vehicles, service records, and maintenance reminders. The system provides full CRUD operations for multiple domain entities and offers a clean, intuitive UI built with Thymeleaf.

Technologies Used

Java 17

Spring Boot / Spring MVC

Spring Data JPA & Hibernate

Thymeleaf

MySQL

Maven

HTML / CSS

How to Run

Clone the repository

Configure database credentials in application.properties

Run the application

Open http://localhost:8080 in your browser

Main Domain Entities

Vehicle

ServiceRecord

Reminder

Valid Domain Functionalities

1. Create Vehicle

Trigger: Add Vehicle form

Endpoint: POST /vehicles/add

CRUD: Create

Result: Vehicle appears in list

2. Edit Vehicle

Trigger: Edit Vehicle form

Endpoint: POST /vehicles/edit/{id}

CRUD: Update

Result: Updated vehicle displayed

3. Delete Vehicle

Trigger: Delete button

Endpoint: POST /vehicles/delete/{id}

CRUD: Delete

Result: Flash message + removed from list

4. Create Service Record

Trigger: Add Service Record form

Endpoint: POST /service-records/add

CRUD: Create

Result: Record appears in list

Database Diagram

User (1) → Vehicle (∞) Vehicle (1) → ServiceRecord (∞) Vehicle (1) → Reminder (∞)

User Roles

USER – full access to personal vehicles, service records, and reminders
