package com.example.parking_control_system.controller;

import com.example.parking_control_system.dto.CarEntryRequestDto;
import com.example.parking_control_system.entity.Car;
import com.example.parking_control_system.entity.ParkingRecord;
import com.example.parking_control_system.entity.Reservation;
import com.example.parking_control_system.repository.CarRepository;
import com.example.parking_control_system.response.ApiResponse;
import com.example.parking_control_system.service.CarService;
import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
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

        ParkingRecord newParkingRecord = new ParkingRecord();

        String carId = carEntryRequestDto.getCarId();
        LocalDateTime entryTime = carEntryRequestDto.getEntryTime();
        String memberId = null;

        Boolean isAlreadyParked = carService.existCarIdAndExitTimeIsNull(carId);

        if (isAlreadyParked) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(3, "출차되지 않은 기록이 있습니다.", null));
        }

        Optional<String> optionalMemberId = carService.getMemberIdByCarId(carId);

        Optional<List<Reservation>> optionalReservations = Optional.empty();

//        유저가 있는 경우
        if (optionalMemberId.isPresent()) {
            memberId = optionalMemberId.get();

//              입차 시간에 맞는 예약 기록들을 리스트로 모두 가져옴
            optionalReservations = carService.getReservationByMemberIdAndTime(memberId, entryTime);
        }

//        예약 기록이 있는 경우
        if (optionalReservations.isPresent()) {
            List<Reservation> reservations = optionalReservations.get();

            for (Reservation r : reservations) {
//                입차하지 않은 예약의 경우
                if (r.getStatus().equals(ReservationStatus.PENDING)) {
                    String spaceName = carService.getParkingSpaceByReservation(r.getReservationId());

                    ReservationStatus reservationStatus = ReservationStatus.PENDING;

                    if (!spaceName.equals("0")) {
                        reservationStatus = carService.setReservationStatusConfirm(r.getReservationId());
                    }

//                    모든것이 정상 작동하여 예약된 자리를 배정한 경우
                    if (reservationStatus.equals(ReservationStatus.CONFIRM)) {
                        Boolean carRecordAndSave = carService.createCarRecordAndSave(memberId, spaceName, carId, entryTime, null);

                        if (carRecordAndSave) {
                            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "예약된 자리 배정", spaceName));
                        } else {
                            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(2, "주차 기록 저장 실패", null));
                        }
                    }

                }
            }
        }

        String carSpaceName = carService.getCarSpaceAndSetStatus(ParkingStatus.AVAILABLE, ParkingStatus.OCCUPIED);

//        배정 가능한 자리가 없는 경우
        if (carSpaceName.equals("0")) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "주차 가능한 자리가 없습니다.", null));
        }

        Boolean carRecordAndSave = carService.createCarRecordAndSave(memberId, carSpaceName, carId, entryTime, null);

        if (carRecordAndSave) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "새로운 자리가 배정되었습니다.", carSpaceName));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(2, "주차 기록 저장 실패", null));
        }
    }
}
