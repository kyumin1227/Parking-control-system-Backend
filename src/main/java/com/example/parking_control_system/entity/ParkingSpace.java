package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ParkingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ParkingSpace {
    @Id
    private Integer spaceId;
    @Enumerated(EnumType.STRING)
    private ParkingStatus status;
    @Enumerated(EnumType.STRING)
    private ParkingType type;
    private String areaId;
    private String spaceName;
}
