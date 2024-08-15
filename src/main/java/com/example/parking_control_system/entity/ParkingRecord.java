package com.example.parking_control_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long parkingRecordId;
    private String memberId;
    private Integer spaceId;
    private String carId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

}
