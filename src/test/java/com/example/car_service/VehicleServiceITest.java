package com.example.car_service;

import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.repo.VehicleRepository;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.dtos.VehicleAddRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VehicleServiceITest {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    void addVehicle_happyPath() {
        VehicleAddRequest vehicleAddRequest = VehicleAddRequest.builder()
                .vin("12345678901234567")
                .model("BMW")
                .make("BMW")
                .year(2020)
                .mileage(100000)
                .build();
        User user = User.builder()
                .username("testUser")
                .password("")
                .email("")
                .firstName("Test")
                .lastName("User")
                .active(true)
                .createdAt(LocalDate.now())
                .role(UserRole.USER)
                .build();
        userService.save(user);
        vehicleService.addVehicle(vehicleAddRequest, user);
        assertEquals(1, vehicleRepository.count());
        assertEquals("BMW", vehicleRepository.findByVin(vehicleAddRequest.getVin()).get().getModel());
    }

}
