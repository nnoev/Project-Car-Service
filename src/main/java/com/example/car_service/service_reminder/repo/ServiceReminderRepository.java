package com.example.car_service.service_reminder.repo;

import com.example.car_service.service_reminder.model.ServiceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceReminderRepository extends JpaRepository<ServiceReminder, UUID> {
    Integer deleteAllByUserId(UUID userId);

    Integer deleteAllByVehicleId(UUID vehicleId);

}
