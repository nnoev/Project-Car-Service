package com.example.Car_Service.vehicle.service;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.repo.VehicleRepository;
import com.example.Car_Service.web.dtos.VehicleAddRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserService userService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository , UserService userService) {
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
    }

    public void addVehicle (VehicleAddRequest vehicleAddRequest, HttpSession session) {
        Object userId = session.getAttribute("userId");
        User user = userService.getById((UUID) userId);
        Optional<Vehicle> byVin = vehicleRepository.findByVin(vehicleAddRequest.getVin());
        if (byVin.isPresent()) {
            throw new RuntimeException("A vehicle with this VIN already exists");
        }

        Vehicle vehicle = Vehicle.builder()
                .model(vehicleAddRequest.getModel())
                .make(vehicleAddRequest.getMake())
                .year(vehicleAddRequest.getYear())
                .vin(vehicleAddRequest.getVin())
                .mileage(vehicleAddRequest.getMileage())
                .owner(user)
                .build();
        vehicleRepository.save(vehicle);
        log.info("User %s added vehicle %s %s".formatted(user.getUsername(), vehicle.getMake(), vehicle.getModel()));

    }

}