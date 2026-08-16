package com.example.car_service_microservice.service_record.controller;

import com.example.car_service_microservice.service_record.dto.ErrorResponse;
import com.example.car_service_microservice.service_record.exceptions.ServiceRecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ServiceRecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ServiceRecordNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        log.error("ServiceRecordNotFoundException : {}", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalControllerAdvice(Exception e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        log.error("Exception : {}",e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
