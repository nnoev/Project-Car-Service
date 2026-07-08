package com.example.car_service.exeptions;

public class LimitException extends RuntimeException{
    public LimitException(String message) {
        super(message);
    }

}
