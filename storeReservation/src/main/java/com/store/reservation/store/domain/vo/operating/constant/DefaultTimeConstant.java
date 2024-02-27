package com.store.reservation.store.domain.vo.operating.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public enum DefaultTimeConstant {
    OPENING_HOURS(LocalTime.of(9, 0)),
    CLOSING_HOURS(LocalTime.of(22, 0)),
    BREAK_START_TIME(LocalTime.of(0, 0)),
    BREAK_END_TIME(LocalTime.of(0, 0)),
    ;
    private final LocalTime time;
}
