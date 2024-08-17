package com.example.parking_control_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CarExitRequestDto {
    private String carId;
    private LocalDateTime exitTime;
}
