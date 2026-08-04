package com.example.car_service_microservice.service_record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateServiceRecordRequest {

    private LocalDate serviceDate;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    private String description;

    @NotNull
    @Positive(message = "Cost must be positive")
    private BigDecimal cost;

    @Positive(message = "Mileage must be positive")
    private int mileageAtService;

}
