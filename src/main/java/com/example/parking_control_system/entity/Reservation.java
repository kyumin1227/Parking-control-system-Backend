package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reservationId;
    private String memberId;
    private Integer spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime reservationTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
