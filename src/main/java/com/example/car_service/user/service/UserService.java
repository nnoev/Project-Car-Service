package com.example.car_service.user.service;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserClass;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.repo.UserRepository;
import com.example.car_service.web.dtos.UserRegistration;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
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
        if (userRegistration.getFirstName().equals("Guest")) {
            user.setRole(UserRole.GUEST);
        }
        userRepository.save(user);
        log.info("User {} registered successfully", userRegistration.getUsername());
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NothingFoundException("User does not exist"));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @CacheEvict(cacheNames = "adminSummary", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(User user) {
        userRepository.delete(user);
        log.info("User {} deleted successfully by administrator", user.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changeRole(UUID id, UserRole role) {
        User user = getById(id);
        if (user.getRole() == UserRole.ADMIN) {
            log.warn("Unauthorized action: User {} cannot be changed to {}", user.getUsername(), role);
            throw new UnauthorizedActionException("You cannot change an administrator role");
        }
        user.setRole(role);
        userRepository.save(user);
        log.info("User {} role changed to {} by administrator", user.getUsername(), role);
    }

    public User getDeletableUser(UUID id) {
        User user = getById(id);
        if (user.getRole() == UserRole.ADMIN) {
            log.warn("Unauthorized action: User {} cannot be deleted", user.getUsername());
            throw new UnauthorizedActionException("You cannot delete an administrator");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NothingFoundException("User does not exist"));
        return new UserData(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    public void checkForEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration failed: Email {} already exists", email);
            throw new DuplicateException("Email already exists");
        }
    }

    public int updateUserClass() {
        List<User> users = userRepository.findAllByRole(UserRole.USER);
        int count = 0;
        for (User user : users) {
            long age = accountAge(user.getCreatedAt());
            if (age >= 30 && age < 365 && user.getClassType() == UserClass.NEW) {
                user.setClassType(UserClass.REGULAR);
                count++;
            }
            if (age >= 365 && user.getClassType() == UserClass.REGULAR) {
                user.setClassType(UserClass.VIP);
                count++;
            }
        }
        userRepository.saveAll(users);
        log.info("{} users class type updated successfully", count);
        return count;
    }

    public long accountAge(LocalDate date) {
        return ChronoUnit.DAYS.between(date, LocalDate.now());
    }
    @Transactional
    public User getGuest() {
        UserRegistration userRegistration = new UserRegistration();
        long guestNumber = 0;
        while (userRepository.findByUsername("guest%d".formatted(guestNumber)).isPresent()) {
            guestNumber++;
        }
        userRegistration.setUsername("guest" + guestNumber);
        userRegistration.setEmail("guest" + guestNumber + "@car-service.com");
        userRegistration.setPassword("guest" + guestNumber);
        userRegistration.setFirstName("Guest");
        userRegistration.setLastName("User" + guestNumber);
        registerUser(userRegistration);
        return userRepository.findByUsername("guest" + guestNumber).get();
    }

}
