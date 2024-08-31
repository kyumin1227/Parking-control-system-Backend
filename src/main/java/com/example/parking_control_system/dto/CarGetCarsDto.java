package com.example.parking_control_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class CarGetCarsDto {

    private Long parkingRecordId;
    private Long memberId;
    private Integer spaceId;
    private String carId;
    private LocalDateTime entryTime;
    private String spaceName;

}
