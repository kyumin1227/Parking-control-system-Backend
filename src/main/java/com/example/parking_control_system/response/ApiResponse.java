package com.example.parking_control_system.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse {
    private Integer code;
    private String message;
    private Object data;

    public ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
