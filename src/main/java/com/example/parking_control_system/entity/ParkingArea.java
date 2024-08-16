package com.example.parking_control_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ParkingArea {
    @Id
    private Character areaId;
    private Integer allSpace;
    private Integer occupiedSpace;
    private Integer reservationSpace;
}
