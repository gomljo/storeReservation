package com.store.reservation.reservation.constants.time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TimeLimit {

    TEN(10),
    ONE_HOUR(60),
    FIVE(5)
    ;
    private final int minute;
}
