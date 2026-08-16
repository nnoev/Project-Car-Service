package com.example.car_service.service_reminder.service;

import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.service_reminder.model.ServiceReminder;
import com.example.car_service.service_reminder.repo.ServiceReminderRepository;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.web.dtos.ServiceReminderDto;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
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
        return serviceReminderRepository.findById(id).orElseThrow(()->new NothingFoundException("Reminder does not exist"));
    }

    public void deleteServiceReminder(ServiceReminder serviceReminder) {
        serviceReminderRepository.delete(serviceReminder);
    }

    public void addReminder(@Valid ServiceReminderDto reminderDto, Vehicle vehicle, User user) {
        if(user.getRole() == UserRole.GUEST || vehicle.getServiceReminders().size() >= 3 ){
            throw new LimitException("Vehicle has reached maximum number of service reminders");
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<ServiceReminder> getAllReminders() {
        return serviceReminderRepository.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReminder(ServiceReminder serviceReminder) {
        serviceReminderRepository.delete(serviceReminder);
    }

}
