package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
}
