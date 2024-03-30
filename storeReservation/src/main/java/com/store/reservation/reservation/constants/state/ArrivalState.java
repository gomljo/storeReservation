package com.store.reservation.reservation.constants.state;

import java.util.Arrays;

public enum ArrivalState {

    READY,
    ARRIVED,
    NO_SHOW;

    public static boolean validate(String arrivalState){
        return Arrays.stream(ArrivalState.values()).anyMatch(state -> state.name().equals(arrivalState));
    }

}
