package com.example.Car_Service.service_record.service;

import com.example.Car_Service.service_record.model.ServiceRecord;
import com.example.Car_Service.service_record.repo.ServiceRecordRepository;
import com.example.Car_Service.user.model.User;
import com.example.Car_Service.vehicle.model.Vehicle;
import com.example.Car_Service.web.dtos.ServiceRecordAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;

    @Autowired
    public ServiceRecordService(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    public void addService(ServiceRecordAddRequest serviceRecordAddRequest, Vehicle vehicle, User user) {
        ServiceRecord serviceRecord = ServiceRecord.builder()
                .vehicle(vehicle)
                .user(user)
                .serviceDate(serviceRecordAddRequest.getDate())
                .serviceType(serviceRecordAddRequest.getServiceType())
                .description(serviceRecordAddRequest.getDescription())
                .cost(serviceRecordAddRequest.getCost())
                .mileageAtService(serviceRecordAddRequest.getMileageAtService())
                .build();
        serviceRecordRepository.save(serviceRecord);
    }

}
