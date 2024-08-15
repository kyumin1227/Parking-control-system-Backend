package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Character> {
}
