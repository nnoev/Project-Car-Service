package com.example.Car_Service.service_record.repo;

import com.example.Car_Service.service_record.model.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, UUID> {

}
