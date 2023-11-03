package com.backend.testManagement.dto;

public class ValidationUtilsDTO {
    public static void validatePageParameters(int pageNo, int pageSize) {
        if (pageNo < 0 || pageSize < 1) {
            throw new IllegalArgumentException("Invalid page parameters");
        }
    }
}