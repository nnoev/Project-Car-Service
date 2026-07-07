package com.example.Car_Service.service_reminder.service;

import com.example.Car_Service.service_reminder.model.ServiceReminder;
import com.example.Car_Service.service_reminder.repo.ServiceReminderRepository;

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

}
