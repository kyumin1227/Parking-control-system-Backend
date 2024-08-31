package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.CarType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Car {
    @Id
    private String carId;
    @Enumerated(EnumType.STRING)
    private CarType carType;
    private Long memberId;
}
