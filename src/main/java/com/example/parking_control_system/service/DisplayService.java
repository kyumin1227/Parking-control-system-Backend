package com.example.parking_control_system.service;

import com.example.parking_control_system.entity.ParkingArea;
import com.example.parking_control_system.repository.ParkingAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class DisplayService {

    private final ParkingAreaRepository parkingAreaRepository;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public List<ParkingArea> getInitialParkingAreaInfo() {

        List<ParkingArea> all = parkingAreaRepository.findAll();

        // 최초 연결 시 전송할 데이터
        return all;
    }

    public void addEmitter(SseEmitter emitter) {
        this.emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        this.emitters.remove(emitter);
    }

    public void notifyParkingAreaUpdate(ParkingArea parkingArea) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("update").data(parkingArea));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    // 이 메서드는 ParkingArea가 변경될 때 호출됩니다.
    public void updateParkingArea(ParkingArea parkingArea) {
        notifyParkingAreaUpdate(parkingArea);
    }

}
