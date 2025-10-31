package com.orchestra.api.dto.response;
import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Геттеры и сеттеры (можно добавить lombok @Data)

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = "OK";
        response.data = data;
        response.timestamp = LocalDateTime.now();
        return response;
    }
}
