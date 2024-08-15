package com.example.parking_control_system.controller;

import com.example.parking_control_system.entity.ParkingSpace;
import com.example.parking_control_system.repository.MemberRepository;
import com.example.parking_control_system.repository.ParkingSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class DisplayController {


    private final ParkingSpaceRepository parkingSpaceRepository;

    /**
     * 테스트 (1초 간격으로 40회 현재 시간과 카운트 출력)
     */
    @GetMapping(value = "/api/display/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter test() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                for (int i = 0; i < 40; i++) {
                    emitter.send("SSE event - " + LocalTime.now() + " count = " + Integer.toString(i + 1));
                    TimeUnit.SECONDS.sleep(1); // 1초 간격으로 데이터 전송
                }
                emitter.complete(); // 작업 완료 후 SSE 스트림 종료
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e); // 오류 발생 시 스트림 종료
            }
        });

        return emitter;
    }
}
