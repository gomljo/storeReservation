package com.store.reservation.reservation.exception.reservation;

import com.store.reservation.exception.CustomRuntimeException;

public class ReservationRuntimeException extends CustomRuntimeException {

    private final ReservationError reservationError;
    private final String description;

    public ReservationRuntimeException(ReservationError reservationError) {
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
