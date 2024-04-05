package com.store.reservation.reservation.exception.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationError {

    NO_SUCH_RESERVATION("해당 예약은 존재하지 않습니다."),
    ALREADY_RESERVED("이미 예약되었습니다."),
    ILLEGAL_ACCESS("올바른 사용자 접근이 아닙니다."),
    NOT_AVAILABLE("예약 가능하지 않습니다."),
    ILLEGAL_RESERVATION_TIME("예약 시간이 유효하지 않습니다")
    ;

    private final String description;

}
