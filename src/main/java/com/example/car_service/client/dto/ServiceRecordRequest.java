package com.example.car_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRecordRequest {

    private UUID id;

    private UUID vehicleId;

    private UUID userId;

    private LocalDate serviceDate;

    private String serviceType;

    private String description;

    private BigDecimal cost;

    private int mileageAtService;

}
