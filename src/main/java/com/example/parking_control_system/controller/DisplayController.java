package com.example.parking_control_system.controller;

import com.example.parking_control_system.entity.ParkingSpace;
import com.example.parking_control_system.repository.MemberRepository;
import com.example.parking_control_system.repository.ParkingSpaceRepository;
import com.example.parking_control_system.service.DisplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DisplayController {


    private final ParkingSpaceRepository parkingSpaceRepository;
    private final DisplayService displayService;

    /**
     * 테스트 (1초 간격으로 40회 현재 시간과 카운트 출력)
     */
    @GetMapping(value = "/display/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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


    /**
     * 주차 자리 상태를 반환하는 sse
     * 처음 연결 시 전체 정보를 넘기고 이후 상태 변경 시 변경된 구역 단위로 정보를 전송
     * @return
     */
    @GetMapping(value = "/display/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendStatusInfo() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("initial")
                        .data(displayService.getInitialParkingAreaInfo()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        executor.shutdown();

        emitter.onCompletion(() -> displayService.removeEmitter(emitter));
        emitter.onTimeout(() -> displayService.removeEmitter(emitter));

        displayService.addEmitter(emitter);

        return emitter;
    }
}
