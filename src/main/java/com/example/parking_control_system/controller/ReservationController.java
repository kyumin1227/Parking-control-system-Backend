package com.example.parking_control_system.controller;

import com.example.parking_control_system.entity.Member;
import com.example.parking_control_system.entity.Reservation;
import com.example.parking_control_system.response.ApiResponse;
import com.example.parking_control_system.service.CarService;
import com.example.parking_control_system.service.MemberService;
import com.example.parking_control_system.service.ReservationService;
import com.example.parking_control_system.type.ParkingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final MemberService memberService;
    private final CarService carService;
    private final ReservationService reservationService;

    /**
     * 예약 기록 생성
     * @param authentication
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse> postReservation(
            Authentication authentication) {

        Object principal = authentication.getPrincipal();
        Member memberByEmail = memberService.getMemberByEmail((String) principal);
        Long memberId = memberByEmail.getMemberId();
        String parkingSpaceName = carService.getCarSpaceAndSetStatus(ParkingStatus.AVAILABLE, ParkingStatus.RESERVED);

        System.out.println("parkingSpaceName = " + parkingSpaceName);

//        예약 가능한 자리가 없는 경우
        if (parkingSpaceName.equals("0")) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "예약 가능한 자리가 없습니다.", null));
        }

        Reservation reservation = reservationService.makeReservation(memberId, parkingSpaceName);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "예약 되었습니다.", reservation));

    }



}
