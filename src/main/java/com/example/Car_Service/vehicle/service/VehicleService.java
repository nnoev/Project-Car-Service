package com.example.Car_Service.vehicle.service;

import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.vehicle.repo.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findByOwner(UUID ownerId) {
        return vehicleRepository.findByUserId(ownerId);
    }

}