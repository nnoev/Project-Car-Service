package com.example.car_service_microservice.service_record.service;

import com.example.car_service_microservice.service_record.dto.ServiceRecordRequest;
import com.example.car_service_microservice.service_record.dto.ServiceRecordResponse;
import com.example.car_service_microservice.service_record.dto.UpdateServiceRecordRequest;
import com.example.car_service_microservice.service_record.exceptions.ServiceRecordNotFoundException;
import com.example.car_service_microservice.service_record.repository.ServiceRecordEntity;
import com.example.car_service_microservice.service_record.repository.ServiceRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;

    @Autowired
    public ServiceRecordService(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    public ServiceRecordResponse create(ServiceRecordRequest request) {
        ServiceRecordEntity record = ServiceRecordEntity.builder()
                .vehicleId(request.getVehicleId())
                .userId(request.getUserId())
                .serviceDate(request.getServiceDate())
                .serviceType(request.getServiceType())
                .description(request.getDescription())
                .cost(request.getCost())
                .mileageAtService(request.getMileageAtService())
                .build();
        ServiceRecordEntity savedRecord = serviceRecordRepository.save(record);
        return mapToResponse(savedRecord);
    }

    public ServiceRecordResponse getById(UUID recordId, UUID userId) {
        ServiceRecordEntity serviceRecordEntity = serviceRecordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(
                        () -> new ServiceRecordNotFoundException("Service Record not found")
                );
        return mapToResponse(serviceRecordEntity);
    }

    private ServiceRecordResponse mapToResponse(ServiceRecordEntity serviceRecordEntity) {
        return ServiceRecordResponse.builder()
                .id(serviceRecordEntity.getId())
                .userId(serviceRecordEntity.getUserId())
                .vehicleId(serviceRecordEntity.getVehicleId())
                .serviceDate(serviceRecordEntity.getServiceDate())
                .serviceType(serviceRecordEntity.getServiceType())
                .description(serviceRecordEntity.getDescription())
                .cost(serviceRecordEntity.getCost())
                .mileageAtService(serviceRecordEntity.getMileageAtService()).build();
    }

    public List<ServiceRecordResponse> getAllByUserId(UUID userId) {
        return serviceRecordRepository
                .findAllByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ServiceRecordResponse update(UUID recordId, UUID userId, UpdateServiceRecordRequest request) {
        ServiceRecordEntity serviceRecordEntity = serviceRecordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new ServiceRecordNotFoundException("Service Record not found"));
        serviceRecordEntity.setServiceDate(request.getServiceDate());
        serviceRecordEntity.setServiceType(request.getServiceType());
        serviceRecordEntity.setDescription(request.getDescription());
        serviceRecordEntity.setCost(request.getCost());
        serviceRecordEntity.setMileageAtService(request.getMileageAtService());
        serviceRecordRepository.save(serviceRecordEntity);
        return mapToResponse(serviceRecordEntity);
    }

    public ServiceRecordResponse delete(UUID recordId, UUID userId) {
        ServiceRecordEntity serviceRecordEntity = serviceRecordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new ServiceRecordNotFoundException("Service Record not found"));
        serviceRecordRepository.delete(serviceRecordEntity);
        return mapToResponse(serviceRecordEntity);
    }

    public Integer getCount() {
        List<ServiceRecordEntity> all = serviceRecordRepository.findAll();
        return all.size();
    }

    public BigDecimal totalCost() {
        List<ServiceRecordEntity> all = serviceRecordRepository.findAll();
        if (all.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalCost = BigDecimal.ZERO;
        for (ServiceRecordEntity record : all) {
            totalCost = totalCost.add(record.getCost());
        }
        return totalCost;
    }

    public List<ServiceRecordResponse> getAll() {
        return serviceRecordRepository.findAll().stream().map(this::mapToResponse).toList();
    }

}

