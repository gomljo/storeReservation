package com.store.reservation.reservation.constants.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Duration {
    THIS_WEEK(TimeUnit.WEEK,1),
    ONE_MONTH(TimeUnit.MONTH,1),
    THREE_MONTH(TimeUnit.MONTH,3),
    SIX_MONTH(TimeUnit.MONTH,6),
    ONE_YEAR(TimeUnit.YEAR,1),
    TOTAL(TimeUnit.TOTAL,0);
    private final TimeUnit timeUnit;
    private final int howManyPast;


    public static boolean validate(String value) {
        return Arrays.stream(Duration.values()).anyMatch(duration -> duration.name().equals(value));
    }
}
