package com.example.parking_control_system.service;

import com.example.parking_control_system.entity.Car;
import com.example.parking_control_system.entity.Member;
import com.example.parking_control_system.entity.Reservation;
import com.example.parking_control_system.repository.CarRepository;
import com.example.parking_control_system.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

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
     * 입차 시간과 멤버의 아이디를 통해 예약 기록을 확인
     * @param memberId
     * @param time
     * @return
     */
    public Optional<Reservation> getReservationByMemberIdAndTime(String memberId, LocalDateTime time) {

        Optional<Reservation> optionalReservation = reservationRepository.findByMemberIdAndStartTimeBeforeAndEndTimeAfter(memberId, time, time);

        return optionalReservation;
    }


    /**
     * 예약 번호를 통해 주차할 자리를 출력(문자열)
     * 만약 예약이 취소되어 있는 경우에는 "0"을 출력
     * @param reservationId
     * @return
     */
    public Optional<String> getParkingSpaceByReservation(Long reservationId) {

        return Optional.of("0");
    }

}
