package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByMemberIdAndStartTimeBeforeAndEndTimeAfter(String memberId, LocalDateTime startTime, LocalDateTime endTime);
}
