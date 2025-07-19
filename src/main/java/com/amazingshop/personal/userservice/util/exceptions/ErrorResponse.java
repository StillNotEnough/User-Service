package com.amazingshop.personal.userservice.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private long timestamp;
    private String path;
    private int status;

    public ErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse makeErrorResponse(String message){
        return new ErrorResponse(message, System.currentTimeMillis());
    }

    public static ErrorResponse makeErrorResponse(String message, String path, int status) {
        return new ErrorResponse(message, System.currentTimeMillis(), path, status);
    }
}