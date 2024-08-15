package com.example.parking_control_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ParkingArea {
    @Id
    private Character areaId;
    private Integer allSpace;
    private Integer occupiedSpace;
    private Integer reservationSpace;
}
