package com.store.reservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.store.reservation.exception.GlobalErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e){
        log.error("Exception is occurred.", e);

        return new ErrorResponse(
                INTERNAL_SERVER_ERROR.toString(),
                INTERNAL_SERVER_ERROR.getDescription()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(CustomException customException){
        log.error("사용자 정의 에러 발생", customException);

        return new ErrorResponse(
                customException.getErrorCode(),
                customException.getDescription()
        );
    }
}
