package com.example.car_service.user.service;

import com.example.car_service.user.model.User;
import com.example.car_service.user.property.GuestProperties;
import com.example.car_service.user.property.UserProperties;
import com.example.car_service.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
public class UserInitializer implements ApplicationRunner {

    private final UserProperties userProperties;

    private final GuestProperties guestProperties;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitializer(UserProperties userProperties, GuestProperties guestProperties, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userProperties = userProperties;
        this.guestProperties = guestProperties;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
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
            log.info("Default guest user created successfully");
        }
    }

    private void createAdmin() {
        Optional<User> optionalUser = userRepository.findByUsername(userProperties.getUsername());
        if (optionalUser.isEmpty()) {
            User admin = User.builder()
                    .username(userProperties.getUsername())
                    .password(passwordEncoder.encode(userProperties.getPassword()))
                    .firstName(userProperties.getFirstName())
                    .lastName(userProperties.getLastName())
                    .email(userProperties.getEmail())
                    .role(userProperties.getRole())
                    .createdAt(LocalDate.now())
                    .active(userProperties.isActive())
                    .build();
            userRepository.save(admin);
            log.info("Default admin user created successfully");
        }
    }

}