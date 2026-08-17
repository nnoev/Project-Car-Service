package com.example.car_service.user.scheduler;

import com.example.car_service.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClassScheduler {

    private final UserService userService;

    @Autowired
    public UserClassScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 604800000)
    public int updatingUserClass() {
        userService.updateUserClass();
        log.info("User class updated");
        return userService.updateUserClass();
    }

}
