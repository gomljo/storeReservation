package com.store.reservation.reservation.reservation.exception;

import com.store.reservation.exception.CustomException;

public class ReservationException extends CustomException {

    private final ReservationError reservationError;
    private final String description;

    public ReservationException(ReservationError reservationError) {
        this.reservationError = reservationError;
        this.description = reservationError.getDescription();
    }


    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getErrorCode() {
        return this.reservationError.toString();
    }
}
