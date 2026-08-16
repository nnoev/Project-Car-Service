package com.example.car_service_microservice.service_record.controller;

import com.example.car_service_microservice.service_record.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalControllerAdvice(Exception e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }

}
