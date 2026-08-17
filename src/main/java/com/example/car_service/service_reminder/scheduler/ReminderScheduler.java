package com.example.car_service.service_reminder.scheduler;

import com.example.car_service.service_reminder.service.ServiceReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReminderScheduler {

    private final ServiceReminderService serviceReminderService;

    @Autowired
    public ReminderScheduler(ServiceReminderService serviceReminderService) {
        this.serviceReminderService = serviceReminderService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleReminder() {
        int overdueReminder = serviceReminderService.markOverdueReminders();
        log.info("Overdue reminders: {}", overdueReminder);
    }

}
