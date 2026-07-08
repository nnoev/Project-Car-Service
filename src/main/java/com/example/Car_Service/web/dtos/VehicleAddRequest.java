package com.example.Car_Service.web.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAddRequest {

    private UUID id;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be between 1900 and 2100")
    @Max(value = 2100, message = "Year must be between 1900 and 2100")
    private int year;

    @Positive(message = "Mileage must be positive")
    private int mileage;

    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    private String vin;

}
