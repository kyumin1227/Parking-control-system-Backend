package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.CarType;
import com.example.parking_control_system.type.FeeTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Fee {
    @Id
    private Integer feeId;
    private String feeName;
    private Integer fee;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    @Enumerated(EnumType.STRING)
    private FeeTime feeTime;
}
