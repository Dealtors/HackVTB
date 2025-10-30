package com.orchestra.api.dto.response;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Конструкторы, геттеры, сеттеры
}