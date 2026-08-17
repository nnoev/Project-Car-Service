package com.example.car_service.admin;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.dtos.AdminCaching;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminService {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final ServiceReminderService serviceReminderService;

    private final ServiceRecordClient serviceRecordClient;

    @Autowired
    public AdminService(UserService userService, VehicleService vehicleService, ServiceReminderService serviceReminderService, ServiceRecordClient serviceRecordClient) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.serviceReminderService = serviceReminderService;
        this.serviceRecordClient = serviceRecordClient;
    }

    @Cacheable("adminSummary")
    public AdminCaching getSummary() {
        log.info("Getting admin summary");
        return AdminCaching.builder()
                .users(userService.getAllUsers().size())
                .vehicles(vehicleService.getAllVehicles().size())
                .services(serviceRecordClient.getAll().size())
                .reminders(serviceReminderService.getAllReminders().size())
                .build();
    }

}
