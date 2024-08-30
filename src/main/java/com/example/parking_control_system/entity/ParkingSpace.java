package com.example.parking_control_system.entity;

import com.example.parking_control_system.listener.ParkingSpaceListener;
import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ParkingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = ParkingSpaceListener.class)
public class ParkingSpace {
    @Id
    private Integer spaceId;
    @Enumerated(EnumType.STRING)
    private ParkingStatus status;
    @Enumerated(EnumType.STRING)
    private ParkingType type;
    private Character areaId;
    private String spaceName;
    @Enumerated(EnumType.STRING)
    private ParkingStatus previousStatus;   //  이전 상태 추적을 위해 추가

}
