package com.store.reservation.exception;

import com.store.reservation.apiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.store.reservation.apiResponse.ApiResponse.error;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleException(Exception e) {
        return error(e.getCause().toString(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();

        return error(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler(CustomRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleCustomException(CustomRuntimeException customRuntimeException) {
        return error(customRuntimeException.getErrorCode(), customRuntimeException.getDescription());
    }
}
