package com.example.Car_Service.web.dtos;

import com.example.Car_Service.vehicle.model.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRecordAddRequest {

    @NotNull(message = "Vehicle is required")
    private Long vehicleId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    private String description;

    @Positive(message = "Cost must be positive")
    private double cost;

    @Positive(message = "Mileage must be positive")
    private int mileageAtService;

}
