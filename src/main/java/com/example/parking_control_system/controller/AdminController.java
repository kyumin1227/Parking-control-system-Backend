package com.example.parking_control_system.controller;


import com.example.parking_control_system.dto.CarGetCarsDto;
import com.example.parking_control_system.entity.ParkingRecord;
import com.example.parking_control_system.response.ApiResponse;
import com.example.parking_control_system.service.AdminService;
import com.example.parking_control_system.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final CarService carService;


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
}
