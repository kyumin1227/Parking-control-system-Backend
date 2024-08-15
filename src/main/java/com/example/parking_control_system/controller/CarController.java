package com.example.parking_control_system.controller;

import com.example.parking_control_system.dto.CarEntryRequestDto;
import com.example.parking_control_system.entity.Car;
import com.example.parking_control_system.entity.Reservation;
import com.example.parking_control_system.repository.CarRepository;
import com.example.parking_control_system.response.ApiResponse;
import com.example.parking_control_system.service.CarService;
import com.example.parking_control_system.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 입차, 출차
 */
@RestController
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    /**
     * 입차
     * @param carEntryRequestDto (차량 번호, 입차 시간)
     */
    @PostMapping("/api/entry")
    public ResponseEntity<ApiResponse> entity(@RequestBody CarEntryRequestDto carEntryRequestDto) {

        String carId = carEntryRequestDto.getCarId();
        LocalDateTime entryTime = carEntryRequestDto.getEntryTime();

        Optional<String> optionalMemberId = carService.getMemberIdByCarId(carId);

        Optional<Reservation> optionalReservation = Optional.empty();

//        유저가 있는 경우
        if (optionalMemberId.isPresent()) {
            String memberId = optionalMemberId.get();

            optionalReservation = carService.getReservationByMemberIdAndTime(memberId, entryTime);
        }

//        예약 기록이 있는 경우
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            Long reservationId = reservation.getReservationId();

            carService.getParkingSpaceByReservation(reservationId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "temp", null));
    }
}
