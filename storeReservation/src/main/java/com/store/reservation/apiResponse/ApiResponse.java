package com.store.reservation.apiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.protobuf.Api;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private static final String OK = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";
    private static final Object EMPTY = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OK, data, HttpStatus.OK.toString());
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(OK, null, null);
    }

    public static <T> ApiResponse<T> error(T data, String errorMessage) {
        return new ApiResponse<>(ERROR, data, errorMessage);
    }
    public static <T>ApiResponse<T> error(String errorMessage){
        return new ApiResponse<>(ERROR, null, errorMessage);
    }

    public static <T> ApiResponse<T> fail(T data) {
        return new ApiResponse<>(FAIL, data, HttpStatus.BAD_REQUEST.toString());
    }

}
