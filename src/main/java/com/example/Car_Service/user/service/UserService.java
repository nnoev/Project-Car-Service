package com.example.Car_Service.user.service;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.model.UserClass;
import com.example.Car_Service.user.model.UserRole;
import com.example.Car_Service.user.repo.UserRepository;
import com.example.Car_Service.web.dtos.LoginRequest;
import com.example.Car_Service.web.dtos.UserRegistration;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Username does not exist");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }
        if (!optionalUser.get().isActive()) {
            throw new RuntimeException("Account is not active");
        }
        return optionalUser.get();
    }

    @Transactional
    public void registerUser(UserRegistration userRegistration) {
        Optional<User> optionalUser = userRepository.findByUsername(userRegistration.getUsername());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        optionalUser = userRepository.findByEmail(userRegistration.getEmail());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .username(userRegistration.getUsername())
                .password(passwordEncoder.encode(userRegistration.getPassword()))
                .firstName(userRegistration.getFirstName())
                .lastName(userRegistration.getLastName())
                .email(userRegistration.getEmail())
                .role(UserRole.USER)
                .createdAt(LocalDate.now())
                .classType(UserClass.NEW)
                .active(true).build();
        userRepository.save(user);
        log.info("User [%s] registered successfully".formatted(userRegistration.getUsername()));
    }

    public User getByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Username does not exist"));

    }

    public User getById(UUID id) {
     return    userRepository.findById(id).orElseThrow(()->new RuntimeException("User does not exist"));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUserBySession(HttpSession session) {
        return userRepository.findById((UUID) session.getAttribute("userId")).orElseThrow(()->new RuntimeException("User does not exist"));
    }
}
