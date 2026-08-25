package com.example.car_service.service_reminder;

import com.example.car_service.exceptions.LimitException;
import com.example.car_service.service_reminder.model.ServiceReminder;
import com.example.car_service.service_reminder.repo.ServiceReminderRepository;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.web.dtos.ServiceReminderDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceReminderServiceUTest {

    @Mock
    private ServiceReminderRepository serviceReminderRepository;

    @InjectMocks
    private ServiceReminderService serviceReminderService;

    @Test
    void addReminder_withValidData_shouldSaveMappedReminder() {
        User user = User.builder()
                .username("john")
                .role(UserRole.USER)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .vin("WVWZZZ12345678901")
                .serviceReminders(new ArrayList<>())
                .build();

        ServiceReminderDto dto = new ServiceReminderDto();
        dto.setTitle("Change engine oil");
        dto.setDueDate(LocalDate.now().plusMonths(3));
        dto.setDueMileage(125000);

        serviceReminderService.addReminder(dto, vehicle, user);

        ArgumentCaptor<ServiceReminder> reminderCaptor =
                ArgumentCaptor.forClass(ServiceReminder.class);

        verify(serviceReminderRepository).save(reminderCaptor.capture());

        ServiceReminder savedReminder = reminderCaptor.getValue();

        assertEquals(vehicle, savedReminder.getVehicle());
        assertEquals(user, savedReminder.getUser());
        assertEquals(dto.getTitle(), savedReminder.getTitle());
        assertEquals(dto.getDueDate(), savedReminder.getDueDate());
        assertEquals(dto.getDueMileage(), savedReminder.getDueMileage());
        assertFalse(savedReminder.isCompleted());
    }

    @Test
    void addReminder_whenGuestHasThreeReminders_shouldThrowLimitException() {
        User guest = User.builder()
                .username("guest")
                .role(UserRole.GUEST)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .vin("WVWZZZ12345678901")
                .serviceReminders(List.of(
                        new ServiceReminder(),
                        new ServiceReminder(),
                        new ServiceReminder()
                ))
                .build();

        ServiceReminderDto dto = new ServiceReminderDto();
        dto.setTitle("Change engine oil");

        assertThrows(LimitException.class, () -> serviceReminderService.addReminder(dto, vehicle, guest));

        verify(serviceReminderRepository, never()).save(any(ServiceReminder.class));
    }
}