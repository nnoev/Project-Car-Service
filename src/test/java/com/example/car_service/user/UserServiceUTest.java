package com.example.car_service.user;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.user.model.User;
import com.example.car_service.user.repo.UserRepository;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.dtos.UserRegistration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void whenRegisteringUser_andUserAlreadyExists_thenThrowException() {

        UserRegistration dto = new UserRegistration();
        dto.setUsername("username");
        dto.setPassword("user@car-service.com");
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("user@car-service.com")
                .build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        assertThrows(DuplicateException.class, () -> userService.registerUser(dto));

    }     @Test
    void whenRegisteringUser_andAlreadyExists_thenThrowException() {

        UserRegistration dto = new UserRegistration();
        dto.setUsername("username");
        dto.setPassword("user@car-service.com");
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("user@car-service.com")
                .build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        assertThrows(DuplicateException.class, () -> userService.registerUser(dto));

    }


}
