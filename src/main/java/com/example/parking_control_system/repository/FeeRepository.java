package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Integer> {
}
