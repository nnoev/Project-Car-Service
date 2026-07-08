package com.example.Car_Service.service_reminder.service;

import com.example.Car_Service.service_reminder.model.ServiceReminder;
import com.example.Car_Service.service_reminder.repo.ServiceReminderRepository;
import com.example.Car_Service.user.model.User;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.web.dtos.ServiceReminderDto;
import jakarta.validation.Valid;

import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceReminderService {
    private final ServiceReminderRepository serviceReminderRepository;

    public ServiceReminderService(ServiceReminderRepository serviceReminderRepository) {
        this.serviceReminderRepository = serviceReminderRepository;
    }

    public void save(ServiceReminder reminder) {
        serviceReminderRepository.save(reminder);
    }

    public ServiceReminder getById(UUID id) {
        return serviceReminderRepository.findById(id).orElseThrow(()->new RuntimeException("Reminder does not exist"));
    }

    public void deleteServiceRecord(ServiceReminder serviceReminder) {
        serviceReminderRepository.delete(serviceReminder);
    }

    public void addReminder(@Valid ServiceReminderDto reminderDto, Vehicle vehicle, User user) {
        if(user.getUsername().equals("guest") && vehicle.getServiceReminders().size() >= 3 ){
            throw new RuntimeException("Vehicle has reached maximum number of service reminders");
        }
        ServiceReminder serviceReminder = ServiceReminder.builder()
                .vehicle(vehicle)
                .user(user)
                .title(reminderDto.getTitle())
                .dueDate(reminderDto.getDueDate())
                .dueMileage(reminderDto.getDueMileage())
                .completed(false)
                .build();
        serviceReminderRepository.save(serviceReminder);
    }

}
