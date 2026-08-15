package com.example.car_service_microservice.service_record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecordEntity, UUID> {


    List<ServiceRecordEntity> findAllByUserId(UUID userId);


    Optional<ServiceRecordEntity> findByIdAndUserId(UUID recordId, UUID userId);

}
