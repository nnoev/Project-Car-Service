package com.example.car_service_microservice.service_record.service;

import com.example.car_service_microservice.service_record.dto.ServiceRecordRequest;
import com.example.car_service_microservice.service_record.dto.ServiceRecordResponse;
import com.example.car_service_microservice.service_record.dto.UpdateServiceRecordRequest;
import com.example.car_service_microservice.service_record.exceptions.ServiceRecordNotFoundException;
import com.example.car_service_microservice.service_record.model.ServiceRecord;
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

    @Transactional
    public ServiceRecordResponse create(ServiceRecordRequest request) {
        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicleId(request.getVehicleId())
                .userId(request.getUserId())
                .serviceDate(request.getServiceDate())
                .serviceType(request.getServiceType())
                .description(request.getDescription())
                .cost(request.getCost())
                .mileageAtService(request.getMileageAtService())
                .build();
        serviceRecordRepository.save(serviceRecord);
        return mapToResponse(serviceRecord);
    }

    public ServiceRecordResponse getById(UUID recordId, UUID userId) {
        ServiceRecord serviceRecord = serviceRecordRepository
                .findByIdAndUserId(recordId, userId)
                .orElseThrow(
                        () -> new ServiceRecordNotFoundException("Service Record not found")
                );
        return mapToResponse(serviceRecord);
    }

    private ServiceRecordResponse mapToResponse(ServiceRecord serviceRecord) {
        return ServiceRecordResponse.builder()
                .id(serviceRecord.getId())
                .userId(serviceRecord.getUserId())
                .vehicleId(serviceRecord.getVehicleId())
                .serviceDate(serviceRecord.getServiceDate())
                .serviceType(serviceRecord.getServiceType())
                .description(serviceRecord.getDescription())
                .cost(serviceRecord.getCost())
                .mileageAtService(serviceRecord.getMileageAtService()).build();
    }

    public List<ServiceRecordResponse> getAllByUserId(UUID userId) {
        return serviceRecordRepository
                .findAllByUserId(userId)
                .stream()
                .map(serviceRecord -> ServiceRecordResponse.builder()
                        .id(serviceRecord.getId())
                        .userId(serviceRecord.getUserId())
                        .vehicleId(serviceRecord.getVehicleId())
                        .serviceDate(serviceRecord.getServiceDate())
                        .serviceType(serviceRecord.getServiceType())
                        .description(serviceRecord.getDescription())
                        .cost(serviceRecord.getCost())
                        .mileageAtService(
                                serviceRecord.getMileageAtService()
                        )
                        .build())
                .toList();
    }

    @Transactional
    public ServiceRecordResponse update(UUID recordId, UUID userId, UpdateServiceRecordRequest request) {
        ServiceRecord serviceRecord = serviceRecordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new ServiceRecordNotFoundException("Service Record not found"));
        serviceRecord.setServiceDate(request.getServiceDate());
        serviceRecord.setServiceType(request.getServiceType());
        serviceRecord.setDescription(request.getDescription());
        serviceRecord.setCost(request.getCost());
        serviceRecord.setMileageAtService(request.getMileageAtService());
        serviceRecordRepository.save(serviceRecord);
        return mapToResponse(serviceRecord);
    }

    public ServiceRecordResponse delete(UUID recordId, UUID userId) {
        ServiceRecord serviceRecord = serviceRecordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new ServiceRecordNotFoundException("Service Record not found"));
        serviceRecordRepository.delete(serviceRecord);
        return mapToResponse(serviceRecord);
    }

    public Integer getCount() {
        List<ServiceRecord> all = serviceRecordRepository.findAll();
        return all.size();
    }

    public BigDecimal totalCost() {
        List<ServiceRecord> all = serviceRecordRepository.findAll();
        return all.stream().map(ServiceRecord::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
