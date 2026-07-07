package com.example.Car_Service.web.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAddRequest {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be valid")
    @Max(value = 2100, message = "Year must be valid")
    private int year;

    @Positive(message = "Mileage must be positive")
    private int mileage;

    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    private String vin;

}
