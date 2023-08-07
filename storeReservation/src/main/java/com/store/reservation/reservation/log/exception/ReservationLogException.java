package com.store.reservation.reservation.log.exception;

import com.store.reservation.exception.CustomException;

public class ReservationLogException extends CustomException {

    private final ReservationLogErrorCode errorCode;
    private final String description;

    public ReservationLogException(ReservationLogErrorCode errorCode) {
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }

    @Override
    protected String getDescription() {
        return this.description;
    }

    @Override
    protected String getErrorCode() {
        return this.errorCode.toString();
    }
}
