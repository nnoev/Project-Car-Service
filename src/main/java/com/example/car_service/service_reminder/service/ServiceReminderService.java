package com.example.car_service.service_reminder.service;

import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.service_reminder.model.ServiceReminder;
import com.example.car_service.service_reminder.repo.ServiceReminderRepository;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.web.dtos.ServiceReminderDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ServiceReminderService {

    private final ServiceReminderRepository serviceReminderRepository;

    public ServiceReminderService(ServiceReminderRepository serviceReminderRepository) {
        this.serviceReminderRepository = serviceReminderRepository;
    }

    public void save(ServiceReminder reminder) {
        serviceReminderRepository.save(reminder);
        log.info("Reminder {} saved successfully", reminder.getId());
    }

    public ServiceReminder getById(UUID id) {
        return serviceReminderRepository.findById(id)
                .orElseThrow(() -> new NothingFoundException("Reminder does not exist"));
    }

    public void deleteServiceReminder(ServiceReminder serviceReminder) {
        serviceReminderRepository.delete(serviceReminder);
        log.info("Reminder {} deleted successfully", serviceReminder.getId());
    }

    public void addReminder(@Valid ServiceReminderDto reminderDto, Vehicle vehicle, User user) {
        if (user.getRole() == UserRole.GUEST || vehicle.getServiceReminders().size() >= 3) {
            log.warn("Service reminder failed: Vehicle {} has reached maximum number of service reminders", vehicle.getVin());
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
        log.info("Service reminder added successfully for vehicle {}",vehicle.getVin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ServiceReminder> getAllReminders() {
        return serviceReminderRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReminder(ServiceReminder serviceReminder) {
        serviceReminderRepository.delete(serviceReminder);
        log.info("Reminder {} deleted successfully by administrator", serviceReminder.getId());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllByUserId(UUID userId) {
        serviceReminderRepository.deleteAllByUserId(userId);
        log.info("All reminders of user {} deleted successfully by administrator", userId);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllByVehicleId(UUID id) {
        serviceReminderRepository.deleteAllByVehicleId(id);
        log.info("All reminders of vehicle {} deleted successfully by administrator", id);
    }

}
