package com.example.car_service_microservice.service_record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private LocalDateTime timestamp;

}
