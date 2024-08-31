package com.example.parking_control_system.service;

import com.example.parking_control_system.entity.ParkingRecord;
import com.example.parking_control_system.repository.ParkingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ParkingRecordRepository parkingRecordRepository;

    /**
     * 출차되지 않은 모든 차량의 리스트 가져오는 메소드
     * @return
     */
    public List<ParkingRecord> getAllCars() {

        List<ParkingRecord> allByExitTimeIsNull = parkingRecordRepository.findAllByExitTimeIsNull();

        return allByExitTimeIsNull;
    }

}
