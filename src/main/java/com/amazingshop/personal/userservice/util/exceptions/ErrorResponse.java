package com.amazingshop.personal.userservice.util.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private long timestamp;

    public ErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse makeErrorResponse(String message){
        return new ErrorResponse(message, System.currentTimeMillis());
    }
}
