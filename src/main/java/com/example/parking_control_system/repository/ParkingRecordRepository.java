package com.example.parking_control_system.repository;

import com.example.parking_control_system.entity.ParkingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    Boolean existsByCarIdAndExitTimeIsNull(String carId);
    Optional<ParkingRecord> findByCarIdAndExitTimeIsNull(String carId);
    List<ParkingRecord> findAllByExitTimeIsNull();

    @Query("SELECT pr FROM ParkingRecord pr " +
            "WHERE pr.carId IN :carIds " +
            "AND pr.entryTime BETWEEN :entryStartDate AND :entryEndDate " +
            "AND (pr.exitTime BETWEEN :exitStartDate AND :exitEndDate OR pr.exitTime IS NULL)")
    Page<ParkingRecord> getParkingRecordsByCustomQuery(
            Pageable pageable,
            @Param("carIds") List<String> carIds,
            @Param("entryStartDate") LocalDateTime entryStartDate,
            @Param("entryEndDate") LocalDateTime entryEndDate,
            @Param("exitStartDate") LocalDateTime exitStartDate,
            @Param("exitEndDate") LocalDateTime exitEndDate);
}
