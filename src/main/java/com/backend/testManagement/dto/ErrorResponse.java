package com.backend.testManagement.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;
    private final int statusCode;
    private final String timestamp;

    public ErrorResponse(String message, int statusCode, String timestamp) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }


}
