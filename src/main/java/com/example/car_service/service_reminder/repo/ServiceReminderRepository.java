package com.example.car_service.service_reminder.repo;

import com.example.car_service.service_reminder.model.ServiceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceReminderRepository extends JpaRepository<ServiceReminder, UUID> {

    void deleteAllByUserId(UUID userId);

    void deleteAllByVehicleId(UUID vehicleId);

    List<ServiceReminder> findAllByCompletedFalseAndOverdueFalseAndDueDateBefore(LocalDate date);

}
