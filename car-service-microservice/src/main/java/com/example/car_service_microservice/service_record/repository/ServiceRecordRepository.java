package com.example.car_service_microservice.service_record.repository;

import com.example.car_service_microservice.service_record.model.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, UUID> {


    List<ServiceRecord> findAllByUserId(UUID userId);


    Optional<ServiceRecord> findByIdAndUserId(UUID recordId, UUID userId);

}
