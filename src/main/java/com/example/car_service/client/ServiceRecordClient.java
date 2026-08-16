package com.example.car_service.client;

import com.example.car_service.client.dto.ServiceRecordRequest;
import com.example.car_service.client.dto.ServiceRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "service-record", url = "http://localhost:8081/api/v1/service-records")
public interface ServiceRecordClient {

    @GetMapping("/users/{userId}")
    List<ServiceRecordResponse> getAllByUserId(@PathVariable UUID userId);

    @PostMapping
    ServiceRecordResponse create(@RequestBody ServiceRecordRequest request);

    @GetMapping("/{recordId}")
    ServiceRecordResponse getById(@PathVariable UUID recordId, @RequestParam UUID userId);

    @PutMapping("/{recordId}")
    ServiceRecordResponse update(@PathVariable UUID recordId, @RequestParam UUID userId, @RequestBody ServiceRecordRequest request);

    @DeleteMapping("/{id}")
    ServiceRecordResponse delete(@PathVariable UUID id, @RequestParam UUID userId);

    @GetMapping("/count")
    Integer count();

    @GetMapping("/total-cost")
    BigDecimal totalCost();

    @GetMapping("/")
    List<ServiceRecordResponse> getAll();

    @DeleteMapping("/users/{userId}")
    Integer deleteAllByUserId(@PathVariable UUID userId);

    @DeleteMapping("/vehicles/{vehicleId}")
    Integer deleteAllByVehicleId(@PathVariable UUID vehicleId);
}
