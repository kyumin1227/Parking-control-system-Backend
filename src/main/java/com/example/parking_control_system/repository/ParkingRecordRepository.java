package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    Boolean existsByCarIdAndExitTimeIsNull(String carId);
    Optional<ParkingRecord> findByCarIdAndExitTimeIsNull(String carId);
}
