package com.example.parking_control_system.controller;


import com.example.parking_control_system.dto.CarGetCarsDto;
import com.example.parking_control_system.entity.ParkingRecord;
import com.example.parking_control_system.response.ApiResponse;
import com.example.parking_control_system.service.AdminService;
import com.example.parking_control_system.service.CarService;
import com.example.parking_control_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final CarService carService;
    private final MemberService memberService;


    /**
     * 실시간 차량 주차 상황 조회
     * @param carId
     * @return
     */
    @GetMapping("/cars")
    public ResponseEntity<ApiResponse> getCars(@RequestParam(defaultValue = "") String carId) {

        if (carId.equals("")) {
            List<ParkingRecord> allCars = adminService.getAllCars();

            List<CarGetCarsDto> collect = allCars.stream()
                    .map(parkingRecord -> carService.makeCarsDto(parkingRecord))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "전체 데이터 조회", collect));
        }

        Optional<ParkingRecord> optionalParkingRecord = carService.getParkingRecordAndNotExitByCarId(carId);

        if (optionalParkingRecord.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "단일 데이터 조회", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "단일 데이터 조회", optionalParkingRecord.get()));

    }

    @GetMapping("/cars/records")
    public ResponseEntity<ApiResponse> getRecords(
            @RequestParam(defaultValue = "defaultCarId") List<String> carIds,
            @RequestParam(defaultValue = "-1") Long memberId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "parkingRecordId,asc") String[] sort,
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusDays(30)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryStartDate, // 기본값으로 현재 시간에서 30일 전 설정
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryEndDate, // 기본값으로 현재 시간 설정
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusDays(30)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime exitStartDate, // 기본값으로 현재 시간에서 30일 전 설정
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime exitEndDate // 기본값으로 현재 시간 설정
    ) {

//        memberId가 있다면 carId를 멤버의 차량으로 대체
        if (memberId != -1) {

            List<String> carIdsByMemberId = carService.getCarIdsByMemberId(memberId);
            carIds = carIdsByMemberId;
        }

//        carId가 있는 경우
        if (!carIds.isEmpty() && !carIds.get(0).equals("defaultCarId")) {

            Sort.Direction direction = Sort.Direction.fromString(sort[1]);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

            Page<ParkingRecord> parkingRecordByCarId = carService.getParkingRecordByCarIds(carIds, pageable, entryStartDate, entryEndDate, exitStartDate, exitEndDate);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(0, "조회 성공", parkingRecordByCarId));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(1, "해당하는 차량이 없습니다.", null));


    }
}
