package com.example.parking_control_system.service;

import com.example.parking_control_system.entity.*;
import com.example.parking_control_system.repository.CarRepository;
import com.example.parking_control_system.repository.ParkingRecordRepository;
import com.example.parking_control_system.repository.ParkingSpaceRepository;
import com.example.parking_control_system.repository.ReservationRepository;
import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    /**
     * 차량 번호를 통해 멤버 아이디 반환
     * @param carId
     * @return Optional <String>
     */
    public Optional<String> getMemberIdByCarId(String carId) {

        Optional<Car> optionalCar = carRepository.findById(carId);

        if (optionalCar.isEmpty()) {
            return Optional.empty();
        }

        Car car = optionalCar.get();

        String memberId = car.getMemberId();

        return Optional.of(memberId);

    }

    /**
     * 입차 시간과 멤버의 아이디를 통해 예약 기록을 확인 후 리스트로 반환
     * @param memberId
     * @param time
     * @return
     */
    public Optional<List<Reservation>> getReservationByMemberIdAndTime(String memberId, LocalDateTime time) {

        Optional<List<Reservation>> optionalReservations = reservationRepository.findAllByMemberIdAndStartTimeBeforeAndEndTimeAfter(memberId, time, time);

        return optionalReservations;
    }


    /**
     * 예약 번호를 통해 주차할 자리를 출력(문자열)
     * 만약 예약이 취소되어 있는 경우에는 "0"을 출력
     * @param reservationId
     * @return
     */
    public String getParkingSpaceByReservation(Long reservationId) {

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Integer spaceId = optionalReservation.get().getSpaceId();

            Optional<ParkingSpace> optionalParkingSpace = parkingSpaceRepository.findById(spaceId);

            String spaceName = optionalParkingSpace.get().getSpaceName();

            return spaceName;

        }

        return "0";
    }


    /**
     * 예약 id를 받아서 해당 예약의 상태를 확정으로 변경하는 서비스
     * 서비스가 정상 작동 시 CONFIRM을 반환하며 실패 시 PENDING을 반환
     * @param reservationId
     * @return
     */
    public ReservationStatus setReservationStatusConfirm(Long reservationId) {

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(ReservationStatus.CONFIRM);
            Reservation saved = reservationRepository.save(reservation);
            return saved.getStatus();
        }

        return ReservationStatus.PENDING;
    }


    /**
     *  getStatus 상태의 자리를 setStatus 상태로 변경 후 자리 이름 반환
     *  해당 상태르 만족하는 자리가 없는 경우에는 "0" 반환
     * @param getStatus 가져올 상태
     * @param setStatus 변경할 상태
     * @return
     */
    public String getCarSpaceAndSetStatus(ParkingStatus getStatus, ParkingStatus setStatus) {
        List<ParkingSpace> allByStatus = parkingSpaceRepository.findAllByStatus(getStatus);

//        상태를 만족하는 자리가 없는 경우
        if (allByStatus.isEmpty()) {
            return "0";
        }

        Random random = new Random();
        int randomIndex = random.nextInt(allByStatus.size());

        ParkingSpace parkingSpace = allByStatus.get(randomIndex);

        parkingSpace.setStatus(setStatus);

        ParkingSpace save = parkingSpaceRepository.save(parkingSpace);

        if (save.getStatus().equals(setStatus)) {
            return save.getSpaceName();
        }

        return "0";

    }


    /**
     * 주차 기록을 생성 후 성공 시 true 실패 시 false 반환
     * @param memberId
     * @param spaceName
     * @param carId
     * @param entryTime
     * @param exitTime
     * @return
     */
    public Boolean createCarRecordAndSave(String memberId, String spaceName, String carId, LocalDateTime entryTime, LocalDateTime exitTime) {
        ParkingRecord parkingRecord = new ParkingRecord();

        Optional<ParkingSpace> optionalParkingSpace = parkingSpaceRepository.findBySpaceName(spaceName);

        if (optionalParkingSpace.isEmpty()) {
            return false;
        }

        Integer spaceId = optionalParkingSpace.get().getSpaceId();

        parkingRecord.setCarId(carId);
        parkingRecord.setMemberId(memberId);
        parkingRecord.setEntryTime(entryTime);
        parkingRecord.setExitTime(exitTime);
        parkingRecord.setSpaceId(spaceId);

        parkingRecordRepository.save(parkingRecord);

        return true;
    }

}
