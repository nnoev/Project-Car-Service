package com.example.car_service.vehicle.service;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.repo.VehicleRepository;
import com.example.car_service.web.dtos.VehicleAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.UnrecoverableEntryException;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void addVehicle(VehicleAddRequest vehicleAddRequest, User user) {
        if (vehicleRepository.findByVin(vehicleAddRequest.getVin()).isPresent()) {
            throw new DuplicateException("Vehicle already exists");
        }
        if (user.getRole() == UserRole.GUEST && user.getVehicles().size() >= 2) {
            throw new LimitException("User has reached the maximum number of vehicles");
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
        return vehicleRepository.findById(id).orElseThrow(()->new NothingFoundException("Vehicle does not exist"));
    }

    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Vehicle> getAllVehicles() {
       return vehicleRepository.findAll();
    }


    public void checkOwnership(Vehicle vehicle, User user) {
        if (!vehicle.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not have permission to manage this vehicle");
        }
    }

}