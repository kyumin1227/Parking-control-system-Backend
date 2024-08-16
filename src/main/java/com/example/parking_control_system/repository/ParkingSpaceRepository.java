package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.ParkingSpace;
import com.example.parking_control_system.type.ParkingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {

    /**
     * 특정 상태의 자리를 모두 가져옴
     * @param status
     * @return
     */
    List<ParkingSpace> findAllByStatus(ParkingStatus status);
    Optional<ParkingSpace> findBySpaceName(String spaceName);
}
