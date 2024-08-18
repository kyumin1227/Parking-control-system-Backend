package com.example.parking_control_system.dto;

import com.example.parking_control_system.type.CarType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CarEntryRequestDto {
    private String carId;
    private LocalDateTime entryTime;
    private CarType carType;
}
