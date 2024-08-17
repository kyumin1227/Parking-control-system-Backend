package com.example.parking_control_system.listener;

import com.example.parking_control_system.entity.ParkingArea;
import com.example.parking_control_system.repository.ParkingAreaRepository;
import com.example.parking_control_system.service.DisplayService;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ParkingAreaListener {

    private static DisplayService displayService;

    @Autowired
    public void setParkingAreaRepository(DisplayService displayService) {
        ParkingAreaListener.displayService = displayService;
    }

    @PostUpdate
    @Transactional
    public void postUpdate(ParkingArea parkingArea) {
        displayService.updateParkingArea(parkingArea);
    }
}
