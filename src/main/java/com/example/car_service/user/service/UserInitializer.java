package com.example.car_service.user.service;

import com.example.car_service.user.model.User;
import com.example.car_service.user.property.GuestProperties;
import com.example.car_service.user.property.UserProperties;
import com.example.car_service.user.repo.UserRepository;
import com.example.car_service.web.dtos.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class UserInitializer implements ApplicationRunner {

    private final UserService userService;

    private final UserProperties userProperties;

    private final GuestProperties guestProperties;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitializer(UserService userService, UserProperties userProperties, GuestProperties guestProperties, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userProperties = userProperties;
        this.guestProperties = guestProperties;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Optional<User> optionalUser = userRepository.findByUsername(userProperties.getDefaultUser().getUsername());
        if (optionalUser.isEmpty()) {
            UserRegistration userRegistration = UserRegistration.builder().
                    username(userProperties.getDefaultUser().getUsername()).
                    password(userProperties.getDefaultUser().getPassword()).
                    firstName(userProperties.getDefaultUser().getFirstName()).
                    lastName(userProperties.getDefaultUser().getLastName()).
                    email(userProperties.getDefaultUser().getEmail()).
                    build();
            userService.registerUser(userRegistration);
        }
        createGuestUser();
    }

    private void createGuestUser() {
        Optional<User> optionalGuest = userRepository.findByUsername(
                guestProperties.getUsername()
        );
        if (optionalGuest.isEmpty()) {
            User guest = User.builder()
                    .username(guestProperties.getUsername())
                    .password(passwordEncoder.encode(guestProperties.getPassword()))
                    .firstName(guestProperties.getFirstName())
                    .lastName(guestProperties.getLastName())
                    .email(guestProperties.getEmail())
                    .role(guestProperties.getRole())
                    .classType(guestProperties.getClassType())
                    .createdAt(LocalDate.now())
                    .active(guestProperties.isActive())
                    .build();
            userRepository.save(guest);
        }
    }

}
