package com.example.parking_control_system.service;

import com.example.parking_control_system.dto.CarGetCarsDto;
import com.example.parking_control_system.entity.*;
import com.example.parking_control_system.repository.CarRepository;
import com.example.parking_control_system.repository.ParkingRecordRepository;
import com.example.parking_control_system.repository.ParkingSpaceRepository;
import com.example.parking_control_system.repository.ReservationRepository;
import com.example.parking_control_system.type.CarType;
import com.example.parking_control_system.type.ParkingStatus;
import com.example.parking_control_system.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private final MemberService memberService;

    /**
     * 차량 번호를 통해 멤버 아이디 반환
     * @param carId
     * @return Optional <String>
     */
    public Optional<Long> getMemberIdByCarId(String carId) {

        Optional<Car> optionalCar = carRepository.findById(carId);

        if (optionalCar.isEmpty()) {
            return Optional.empty();
        }

        Car car = optionalCar.get();

        Long memberId = car.getMemberId();

        if (memberId == null) {
            return Optional.empty();
        }

        return Optional.of(memberId);

    }

    /**
     * 입차 시간과 멤버의 아이디를 통해 예약 기록을 확인 후 리스트로 반환
     * @param memberId
     * @param time
     * @return
     */
    public Optional<List<Reservation>> getReservationByMemberIdAndTime(Long memberId, LocalDateTime time) {

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
    public Boolean createCarRecordAndSave(Long memberId, String spaceName, String carId, LocalDateTime entryTime, LocalDateTime exitTime) {
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


    /**
     * 차량의 아이디를 통해 해당 차량의 출차되지 않은 기록이 있는지 확인
     * @param carId
     * @return
     */
    public Boolean existCarIdAndExitTimeIsNull(String carId) {

        Boolean b = parkingRecordRepository.existsByCarIdAndExitTimeIsNull(carId);

        return b;
    }


    /**
     * 출차 시 carId를 통해 출차 시간이 없는 주차 기록을 가져오는 서비스
     * @param carId
     * @return
     */
    public Optional<ParkingRecord> getParkingRecordAndNotExitByCarId(String carId) {
        Optional<ParkingRecord> optionalParkingRecord = parkingRecordRepository.findByCarIdAndExitTimeIsNull(carId);

        return optionalParkingRecord;
    }


    /**
     * 차량의 아이디를 통해 차량 타입을 가져오는 서비스
     * @param carId
     * @return
     */
    public CarType getCarTypeByCarId(String carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("차량을 찾을수 없습니다."));

        return car.getCarType();
    }


    /**
     * 요금 계산 서비스
     * 추후 수정 필요
     * @param carType
     * @param duration
     * @return
     */
    public long calFeeByCarTypeAndDuration(CarType carType, Duration duration) {

        long seconds = duration.getSeconds();
        long fee = 0;

        long oneDay = 0;
        long oneHour = 0;
        long tenMinute = 0;

        if (seconds >= 86400) {
            oneDay = seconds / 86400;
        }

        seconds -= 86400 * oneDay;

        if (seconds >= 3600) {
            oneHour = seconds / 3600;
        }

        seconds -= 3600 * oneHour;

        if (seconds > 0) {
            tenMinute = seconds / 600;
            if (seconds % 600 > 0) {
                tenMinute += 1;
            }
        }

        fee = 10000 * oneDay + 1000 * oneHour + 300 * tenMinute;

        if (carType.equals(CarType.대형)) {
            fee *= 2;
        } else if (carType.equals(CarType.경차)) {
            fee /= 2;
        }

        return fee;
    }


    /**
     * spaceId와 상태를 받아서 특정 주차 자리의 상태를 변경하는 서비스
     * @param spaceId
     * @param parkingStatus
     */
    public void setParkingSpaceStatusBySpaceId(Integer spaceId, ParkingStatus parkingStatus) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("주차 자리를 찾을수 없습니다."));

        parkingSpace.setStatus(parkingStatus);

        parkingSpaceRepository.save(parkingSpace);
    }


    public void setExitTimeAndFee(ParkingRecord parkingRecord, LocalDateTime exitTime, long fee) {

        parkingRecord.setExitTime(exitTime);
        parkingRecord.setParkingFee(fee);

        parkingRecordRepository.save(parkingRecord);
    }


    /**
     * car 목록에 해당 car가 없으면 추가
     * @param carId
     * @param carType
     */
    public void createCarIsNotFound(String carId, CarType carType) {

        Optional<Car> optionalCar = carRepository.findById(carId);

        if (optionalCar.isPresent()) {
            return;
        }

        Car car = new Car();

        car.setCarId(carId);
        car.setCarType(carType);

        carRepository.save(car);
    }

    public String getSpaceNameBySpaceId(Integer spaceId) {
        Optional<ParkingSpace> optionalParkingSpace = parkingSpaceRepository.findById(spaceId);

        ParkingSpace parkingSpace = optionalParkingSpace.get();

        return parkingSpace.getSpaceName();
    }


    /**
     * 이메일과 차량 번호를 받아서 해당 유저가 차량의 주인인지 확인
     * @param email
     * @param carId
     * @return
     */
    public Boolean checkMemberEmailAndCarId(String email, String carId) {

        Member memberByEmail = memberService.getMemberByEmail(email);

        Optional<Car> car = carRepository.findById(carId);

        if (car.isEmpty()) {
            return false;
        }

        Long memberIdByCar = car.get().getMemberId();
        Long memberIdByEmail = memberByEmail.getMemberId();

        if (memberIdByCar != null && memberIdByCar.equals(memberIdByEmail)) {
            return true;
        }

        return false;

    }


    public CarGetCarsDto makeCarsDto(ParkingRecord parkingRecord) {

        String spaceName = this.getSpaceNameBySpaceId(parkingRecord.getSpaceId());

        CarGetCarsDto carGetCarsDto = new CarGetCarsDto(parkingRecord.getParkingRecordId(),
                parkingRecord.getMemberId(),
                parkingRecord.getSpaceId(),
                parkingRecord.getCarId(),
                parkingRecord.getEntryTime(),
                spaceName);

        return carGetCarsDto;
    }

}
