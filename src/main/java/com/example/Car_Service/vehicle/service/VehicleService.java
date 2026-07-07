package com.example.Car_Service.vehicle.service;

import com.example.Car_Service.user.model.User;
import com.example.Car_Service.user.service.UserService;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.repo.VehicleRepository;
import com.example.Car_Service.web.dtos.VehicleAddRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final UserService userService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, UserService userService) {
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
    }

    public void addVehicle(VehicleAddRequest vehicleAddRequest, User user) {
        if (vehicleRepository.findByVin(vehicleAddRequest.getVin()).isPresent()) {
            throw new RuntimeException("Vehicle already exists");
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

    }
    public void deleteVehicle(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    public Vehicle getById(UUID id) {
        return vehicleRepository.findById(id).orElseThrow(()->new RuntimeException("Vehicle does not exist"));
    }

    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

}