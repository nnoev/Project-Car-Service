package com.example.car_service.service_record.service;

import com.example.car_service.exceptions.LimitException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.service_record.model.ServiceRecord;
import com.example.car_service.service_record.repo.ServiceRecordRepository;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.web.dtos.ServiceRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;

    @Autowired
    public ServiceRecordService(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    public void addService(ServiceRecordDto serviceRecordDto, Vehicle vehicle, User user) {
        if(user.getRole() == UserRole.GUEST && vehicle.getServiceRecords().size() >= 3 ){
            throw new LimitException("Vehicle has reached maximum number of service records");
        }
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
        return serviceRecordRepository.findById(id).orElseThrow(()->new NothingFoundException("Service Record does not exist"));
    }

    public void save(ServiceRecord serviceRecord) {
        serviceRecordRepository.save(serviceRecord);
    }

    public void deleteServiceRecord(ServiceRecord serviceRecord) {
        serviceRecordRepository.delete(serviceRecord);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ServiceRecord> getAllRecords() {
        return serviceRecordRepository.findAll();
    }

}
