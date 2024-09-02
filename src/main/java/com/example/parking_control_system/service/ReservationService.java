package com.example.parking_control_system.service;

import com.example.parking_control_system.entity.ParkingSpace;
import com.example.parking_control_system.entity.Reservation;
import com.example.parking_control_system.repository.ParkingSpaceRepository;
import com.example.parking_control_system.repository.ReservationRepository;
import com.example.parking_control_system.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 멤버의 아이디와 주차 공간의 이름으로 예약 객체를 생성
     * @param memberId
     * @param parkingSpaceName
     */
    public Reservation makeReservation(Long memberId, String parkingSpaceName) {

        Optional<ParkingSpace> bySpaceName = parkingSpaceRepository.findBySpaceName(parkingSpaceName);

        if (bySpaceName.isEmpty()) {
            return null;
        }

        Integer spaceId = bySpaceName.get().getSpaceId();


        Reservation reservation = new Reservation(null, memberId, spaceId, null, null, LocalDateTime.now(), null, ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);

        return saved;

    }
}
