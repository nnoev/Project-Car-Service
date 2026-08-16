package com.example.car_service.vehicle.service;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.repo.VehicleRepository;
import com.example.car_service.web.dtos.VehicleAddRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final ServiceReminderService serviceReminderService;

    private final ServiceRecordClient serviceRecordClient;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, ServiceReminderService serviceReminderService, ServiceRecordClient serviceRecordClient) {
        this.vehicleRepository = vehicleRepository;
        this.serviceReminderService = serviceReminderService;
        this.serviceRecordClient = serviceRecordClient;
    }

    public void addVehicle(VehicleAddRequest vehicleAddRequest, User user) {
        if (vehicleRepository.findByVin(vehicleAddRequest.getVin()).isPresent()) {
            log.warn("Registration failed: Vehicle {} already exists", vehicleAddRequest.getVin());
            throw new DuplicateException("Vehicle already exists");
        }
        if (user.getRole() == UserRole.GUEST && user.getVehicles().size() >= 2) {
            log.warn("Registration failed: User {} has reached the maximum number of vehicles", user.getUsername());
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
        log.info("Vehicle {} registered successfully", vehicle.getVin());

    }

    public void deleteVehicle(Vehicle vehicle) {
        serviceReminderService.deleteAllByVehicleId(vehicle.getId());
        serviceRecordClient.deleteAllByVehicleId(vehicle.getId());
        vehicleRepository.delete(vehicle);
        log.info("Vehicle {} deleted successfully", vehicle.getVin());
    }

    public Vehicle getById(UUID id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new NothingFoundException("Vehicle does not exist"));
    }

    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        log.info("Vehicle {} saved successfully", vehicle.getVin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVehicleAdmin(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
        log.info("Vehicle {} deleted successfully by administrator", vehicle.getVin());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllByUserId(UUID userId) {
        vehicleRepository.deleteAllByOwnerId(userId);
        log.info("All vehicles of user {} deleted successfully by administrator", userId);
    }

    public void checkOwnership(Vehicle vehicle, User user) {
        if (!vehicle.getOwner().getId().equals(user.getId())) {
            log.warn("Unauthorized action: User {} does not own vehicle {}", user.getUsername(), vehicle.getVin());
            throw new UnauthorizedActionException("You do not have permission to manage this vehicle");
        }
    }

    public void checkForDuplication(String vin) {
       if (vehicleRepository.findByVin(vin).isPresent()) {
           log.warn("Registration failed: Vehicle {} already exists", vin);
           throw new DuplicateException("Vehicle already exists");
       }
    }

}