package com.example.car_service;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.user.model.User;
import com.example.car_service.user.repo.UserRepository;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.dtos.UserRegistration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceITest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    void registerUser_happyPath() {
        UserRegistration userRegistration = UserRegistration.builder()
                .username("NikolayNoev")
                .password("123123")
                .firstName("Nikolay")
                .lastName("Noev")
                .email("n.p.noev@gmail.com")
                .build();
        userService.registerUser(userRegistration);
        User savedUser = userRepository.findByUsername("NikolayNoev").orElseThrow();
        assertEquals("NikolayNoev", savedUser.getUsername());
        assertTrue(passwordEncoder.matches("123123", savedUser.getPassword()));
        assertEquals("Nikolay", savedUser.getFirstName());
        assertEquals("Noev",savedUser.getLastName());
        assertEquals("n.p.noev@gmail.com", savedUser.getEmail());
    }
    @Test
    @Transactional
    void registerUser_unhappyPath() {
        UserRegistration userRegistration = UserRegistration.builder()
                .username("NikolayNoev")
                .password("123123")
                .firstName("Nikolay")
                .lastName("Noev")
                .email("n.p.noev@gmail.com")
                .build();
        userService.registerUser(userRegistration);
        UserRegistration userRegistrationNew = UserRegistration.builder()
                .username("NikolayNoev")
                .password("123123")
                .firstName("Nikolay")
                .lastName("Noev")
                .email("n.p.noev@gmail.com")
                .build();
       assertThrows(DuplicateException.class, () -> userService.registerUser(userRegistrationNew));
    }
}
