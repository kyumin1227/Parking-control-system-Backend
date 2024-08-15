package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, String> {
}
