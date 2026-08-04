package com.example.car_service_microservice.service_record.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "service_records")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID vehicleId;

    @Column(nullable = false)
    private UUID userId;

    @Column
    private LocalDate serviceDate;

    @Column(nullable = false)
    private String serviceType;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private int mileageAtService;

}
