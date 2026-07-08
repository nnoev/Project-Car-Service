package com.example.Car_Service.web.dtos;

import com.example.Car_Service.vehicle.model.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;


@Data
public class ServiceReminderDto {

    private UUID serviceId;

    @NotNull(message = "Vehicle is required")
    private UUID vehicleId;

    @NotBlank(message = "Title is required")
    private String title;

    private LocalDate dueDate;

    private Integer dueMileage;


}
