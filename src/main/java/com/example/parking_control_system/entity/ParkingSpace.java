package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ParkingType;
import jakarta.persistence.*;

@Entity
public class ParkingSpace {
    @Id
    private Integer spaceId;
    @Enumerated(EnumType.STRING)
    private ParkingStatus status;
    @Enumerated(EnumType.STRING)
    private ParkingType type;
    private String areaId;
}
