package com.example.parking_control_system.entity;

import com.example.parking_control_system.listener.ParkingAreaListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = ParkingAreaListener.class)
public class ParkingArea {
    @Id
    private Character areaId;
    private Integer allSpace;
    private Integer occupiedSpace;
    private Integer reservationSpace;
}
