package com.example.car_service_microservice.service_record.exceptions;

public class ServiceRecordNotFoundException extends RuntimeException {

    public ServiceRecordNotFoundException(String message) {
        super(message);
    }

}
