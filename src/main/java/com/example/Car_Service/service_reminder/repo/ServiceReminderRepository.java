package com.example.Car_Service.service_reminder.repo;

import com.example.Car_Service.service_reminder.model.ServiceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceReminderRepository extends JpaRepository<ServiceReminder, UUID> {

}
