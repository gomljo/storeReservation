package com.store.reservation.reservation.constants.state;

import java.util.Arrays;

public enum ReservationState {

    READY,
    APPROVAL,
    DENIAL
    ;

    public static boolean validate(String reservationState){
        return Arrays.stream(ReservationState.values())
                .anyMatch(state -> state.name().equals(reservationState));
    }
}
