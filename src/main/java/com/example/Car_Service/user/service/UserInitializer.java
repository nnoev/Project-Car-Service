package com.example.Car_Service.user.service;



import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.property.UserProperties;
import com.example.Car_Service.user.repo.UserRepository;
import com.example.Car_Service.web.dtos.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInitializer implements ApplicationRunner {

    private final UserService userService;

    private final UserProperties userProperties;

    private final UserRepository userRepository;

    @Autowired
    public UserInitializer(UserService userService, UserProperties userProperties, UserRepository userRepository) {
        this.userService = userService;
        this.userProperties = userProperties;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(userProperties.getDefaultUser().getUsername());
        if (!optionalUser.isPresent()) {
            UserRegistration userRegistration = UserRegistration.builder().
                    username(userProperties.getDefaultUser().getUsername()).
                    password(userProperties.getDefaultUser().getPassword()).
                    firstName(userProperties.getDefaultUser().getFirstName()).
                    lastName(userProperties.getDefaultUser().getLastName()).
                    email(userProperties.getDefaultUser().getEmail()).
                    build();
            userService.registerUser(userRegistration);
        }
    }
}
