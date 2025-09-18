package com.groceryapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API Response wrapper
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {
    
    private boolean success;
    
    private String message;
    
    private T data;
    
    private LocalDateTime timestamp;
    
    private String path;
    
    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(true, "Success", data, LocalDateTime.now(), null);
    }
    
    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data, LocalDateTime.now(), null);
    }
    
    public static <T> ApiResponseDto<T> error(String message) {
        return new ApiResponseDto<>(false, message, null, LocalDateTime.now(), null);
    }
    
    public static <T> ApiResponseDto<T> error(String message, String path) {
        return new ApiResponseDto<>(false, message, null, LocalDateTime.now(), path);
    }
}