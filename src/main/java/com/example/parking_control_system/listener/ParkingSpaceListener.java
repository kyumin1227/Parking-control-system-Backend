package com.example.parking_control_system.listener;

import com.example.parking_control_system.entity.ParkingArea;
import com.example.parking_control_system.entity.ParkingSpace;
import com.example.parking_control_system.repository.ParkingAreaRepository;
import com.example.parking_control_system.type.ParkingStatus;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ParkingSpaceListener {

    private static ParkingAreaRepository parkingAreaRepository;

    @Autowired
    public void setParkingAreaRepository(ParkingAreaRepository parkingAreaRepository) {
        ParkingSpaceListener.parkingAreaRepository = parkingAreaRepository;
    }

    @PostUpdate
    @Transactional
    public void postUpdate(ParkingSpace parkingSpace) {
        System.out.println("Call postUpdate");
        System.out.println("parkingSpace.getPreviousStatus() = " + parkingSpace.getPreviousStatus());
        System.out.println("parkingSpace.getStatus() = " + parkingSpace.getStatus());
        if (!parkingSpace.getPreviousStatus().equals(parkingSpace.getStatus())) {
            ParkingArea parkingArea = parkingAreaRepository.findById(parkingSpace.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Parking area not found"));

            System.out.println("parkingArea.getAreaId() = " + parkingArea.getAreaId());

            if (parkingSpace.getStatus().equals(ParkingStatus.OCCUPIED)) {
                parkingArea.setOccupiedSpace(parkingArea.getOccupiedSpace() + 1);
            } else if (parkingSpace.getStatus().equals(ParkingStatus.RESERVED)) {
                parkingArea.setReservationSpace(parkingArea.getReservationSpace() + 1);
            }

            if (parkingSpace.getPreviousStatus().equals(ParkingStatus.OCCUPIED)) {
                parkingArea.setOccupiedSpace(parkingArea.getOccupiedSpace() - 1);
            } else if (parkingSpace.getPreviousStatus().equals(ParkingStatus.RESERVED)) {
                parkingArea.setReservationSpace(parkingArea.getReservationSpace() - 1);
            }

            parkingSpace.setPreviousStatus(parkingSpace.getStatus());

            System.out.println("parkingArea.getOccupiedSpace() = " + parkingArea.getOccupiedSpace());

            parkingAreaRepository.save(parkingArea);
        }
    }
}
