package com.example.car_service.user.service;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserClass;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.repo.UserRepository;
import com.example.car_service.web.dtos.UserRegistration;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(UserRegistration userRegistration) {
        Optional<User> optionalUser = userRepository.findByUsername(userRegistration.getUsername());
        if (optionalUser.isPresent()) {
            log.warn("Registration failed: Username {} already exists", userRegistration.getUsername());
            throw new DuplicateException("Username already exists");
        }
        optionalUser = userRepository.findByEmail(userRegistration.getEmail());
        if (optionalUser.isPresent()) {
            log.warn("Registration failed: Email {} already exists", userRegistration.getEmail());
            throw new DuplicateException("Email already exists");
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
        log.info("User {} registered successfully",userRegistration.getUsername());
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NothingFoundException("User does not exist"));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(User user) {
        userRepository.delete(user);
        log.info("User {} deleted successfully", user.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changeRole(UUID id, UserRole role) {
        User user = getById(id);
        user.setRole(role);
        userRepository.save(user);
        log.info("User {} role changed to {}", user.getUsername(), role);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NothingFoundException("User does not exist"));
        return new UserData(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

}
