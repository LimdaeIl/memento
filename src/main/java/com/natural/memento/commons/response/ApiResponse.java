package com.natural.memento.commons.response;

public record ApiResponse<T>(
        int status,
        boolean success,
        T data
) {

    public static <T> ApiResponse<T> success(int status, T data) {
        return new ApiResponse<>(status, true, data);
    }

    public static <T> ApiResponse<T> success(int status, boolean isSuccess, T data) {
        return new ApiResponse<>(status, isSuccess, data);
    }

    public static <T> ApiResponse<T> success(int status, boolean isSuccess) {
        return new ApiResponse<>(status, true, null);
    }

}