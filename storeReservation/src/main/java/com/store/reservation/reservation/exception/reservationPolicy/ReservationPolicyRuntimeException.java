package com.store.reservation.reservation.exception.reservationPolicy;

import com.store.reservation.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationPolicyRuntimeException extends CustomRuntimeException {
    private final ReservationPolicyError reservationPolicyError;
    @Override
    public String getDescription() {
        return reservationPolicyError.getDescription();
    }

    @Override
    public String getErrorCode() {
        return reservationPolicyError.name();
    }
}
