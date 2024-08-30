package com.example.parking_control_system.init;

import com.example.parking_control_system.entity.ParkingArea;
import com.example.parking_control_system.entity.ParkingSpace;
import com.example.parking_control_system.entity.Role;
import com.example.parking_control_system.repository.ParkingAreaRepository;
import com.example.parking_control_system.repository.ParkingSpaceRepository;
import com.example.parking_control_system.repository.RoleRepository;
import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ParkingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @PostConstruct
    public void init() {
        // Initialize roles
        if (!roleRepository.existsById(0L)) {
            roleRepository.save(new Role(0L, "ADMIN"));
        }
        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role(1L, "USER"));
        }

        // Initialize parking areas
        if (!parkingAreaRepository.existsById('A')) {
            parkingAreaRepository.save(new ParkingArea('A', 4, 0, 0));
        }
        if (!parkingAreaRepository.existsById('B')) {
            parkingAreaRepository.save(new ParkingArea('B', 4, 0, 0));
        }

        // Initialize parking spaces
        if (!parkingSpaceRepository.existsById(1)) {
            parkingSpaceRepository.save(new ParkingSpace(1, ParkingStatus.AVAILABLE, ParkingType.일반, 'A', "A1", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(2)) {
            parkingSpaceRepository.save(new ParkingSpace(2, ParkingStatus.AVAILABLE, ParkingType.일반, 'A', "A2", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(3)) {
            parkingSpaceRepository.save(new ParkingSpace(3, ParkingStatus.AVAILABLE, ParkingType.일반, 'A', "A3", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(4)) {
            parkingSpaceRepository.save(new ParkingSpace(4, ParkingStatus.AVAILABLE, ParkingType.일반, 'A', "A4", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(5)) {
            parkingSpaceRepository.save(new ParkingSpace(5, ParkingStatus.AVAILABLE, ParkingType.일반, 'B', "B1", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(6)) {
            parkingSpaceRepository.save(new ParkingSpace(6, ParkingStatus.AVAILABLE, ParkingType.일반, 'B', "B2", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(7)) {
            parkingSpaceRepository.save(new ParkingSpace(7, ParkingStatus.AVAILABLE, ParkingType.일반, 'B', "B3", ParkingStatus.AVAILABLE));
        }
        if (!parkingSpaceRepository.existsById(8)) {
            parkingSpaceRepository.save(new ParkingSpace(8, ParkingStatus.AVAILABLE, ParkingType.일반, 'B', "B4", ParkingStatus.AVAILABLE));
        }
    }

}
