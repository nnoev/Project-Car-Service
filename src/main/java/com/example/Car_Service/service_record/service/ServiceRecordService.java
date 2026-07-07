package com.example.Car_Service.service_record.service;

import com.example.Car_Service.service_record.model.ServiceRecord;
import com.example.Car_Service.service_record.repo.ServiceRecordRepository;
import com.example.Car_Service.user.model.User;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.web.dtos.ServiceRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;

    @Autowired
    public ServiceRecordService(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    public void addService(ServiceRecordDto serviceRecordDto, Vehicle vehicle, User user) {
        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicle(vehicle)
                .user(user)
                .date(serviceRecordDto.getDate())
                .serviceType(serviceRecordDto.getServiceType())
                .description(serviceRecordDto.getDescription())
                .cost(serviceRecordDto.getCost())
                .mileageAtService(serviceRecordDto.getMileageAtService())
                .build();
        serviceRecordRepository.save(serviceRecord);
    }

    public ServiceRecord getById(UUID id) {
        return serviceRecordRepository.findById(id).orElseThrow(()->new RuntimeException("Service Record does not exist"));
    }

    public void save(ServiceRecord serviceRecord) {
        serviceRecordRepository.save(serviceRecord);
    }

    public void deleteServiceRecord(ServiceRecord serviceRecord) {
        serviceRecordRepository.delete(serviceRecord);
    }

}
