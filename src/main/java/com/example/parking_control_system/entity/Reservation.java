package com.example.parking_control_system.entity;

import com.example.parking_control_system.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reservationId;
    private Long memberId;
    private Integer spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
