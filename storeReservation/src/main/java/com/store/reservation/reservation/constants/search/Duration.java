package com.store.reservation.reservation.constants.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Duration {
    THIS_WEEK(7),
    ONE_MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    ONE_YEAR(1),
    TOTAL(0);
    private final int howManyPast;

    public static boolean validate(String value) {
        return Arrays.stream(Duration.values()).anyMatch(duration -> duration.name().equals(value));
    }
}
