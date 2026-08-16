package com.example.car_service_microservice.service_record.controller;

import com.example.car_service_microservice.service_record.dto.ServiceRecordRequest;
import com.example.car_service_microservice.service_record.dto.ServiceRecordResponse;
import com.example.car_service_microservice.service_record.dto.UpdateServiceRecordRequest;
import com.example.car_service_microservice.service_record.service.ServiceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-records")
@RequiredArgsConstructor
public class ServiceRecordRestController {

    private final ServiceRecordService serviceRecordService;

    @GetMapping("/")
    public ResponseEntity<List<ServiceRecordResponse>> getAll() {
        List<ServiceRecordResponse> response = serviceRecordService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ServiceRecordResponse> getById(@PathVariable UUID recordId, @RequestParam UUID userId) {
        ServiceRecordResponse response = serviceRecordService.getById(recordId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ServiceRecordResponse> insert(@Valid @RequestBody ServiceRecordRequest request) {
        ServiceRecordResponse response = serviceRecordService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ServiceRecordResponse>> getAllByUserId(@PathVariable UUID userId) {
        List<ServiceRecordResponse> serviceRecords =
                serviceRecordService.getAllByUserId(userId);
        return ResponseEntity.ok(serviceRecords);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<ServiceRecordResponse> update(@PathVariable UUID recordId, @RequestParam UUID userId, @Valid @RequestBody UpdateServiceRecordRequest request) {
        ServiceRecordResponse serviceRecordResponse = serviceRecordService.update(recordId, userId, request);
        return ResponseEntity.ok(serviceRecordResponse);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<ServiceRecordResponse> delete(@PathVariable UUID recordId, @RequestParam UUID userId) {
        ServiceRecordResponse serviceRecordResponse = serviceRecordService.delete(recordId, userId);
        return ResponseEntity.ok(serviceRecordResponse);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount() {
        Integer count = serviceRecordService.getCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/total-cost")
    public ResponseEntity<BigDecimal> totalCost() {
        BigDecimal totalCost = serviceRecordService.totalCost();
        return ResponseEntity.ok(totalCost);
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Integer> deleteAllByUserId(@PathVariable UUID userId) {
        Integer count = serviceRecordService.deleteAllByUserId(userId);
        return ResponseEntity.ok(count);
    }
    @DeleteMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Integer> deleteAllByVehicleId(@PathVariable UUID vehicleId) {
        Integer count = serviceRecordService.deleteAllByVehicleId(vehicleId);
        return ResponseEntity.ok(count);
    }
}
