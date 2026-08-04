package com.example.car_service_microservice.service_record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceRecordResponse {

    private UUID id;

    private UUID userId;

    private UUID vehicleId;

    private LocalDate serviceDate;

    private String serviceType;

    private String description;

    private BigDecimal cost;

    private int mileageAtService;

}
