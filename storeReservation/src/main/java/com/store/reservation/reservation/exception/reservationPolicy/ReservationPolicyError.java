package com.store.reservation.reservation.exception.reservationPolicy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationPolicyError {

    INVALID_RESERVATION_TIME("현재 시간이 예약 시간 10분보다 적게 남았습니다."),
    INVALID_RESERVATION_STATE_UPDATE_TIME("예약 상태 변경 시점이 올바르지 않습니다."),
    INVALID_ARRIVAL_STATE_UPDATE_TIME("도착 상태 변경 시점이 올바르지 않습니다."),
    ;
    private final String description;
}
