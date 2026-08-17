package com.example.car_service.web.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCaching {

    private final int users;
    private final int vehicles;
    private final int services;
    private final int reminders;

}
