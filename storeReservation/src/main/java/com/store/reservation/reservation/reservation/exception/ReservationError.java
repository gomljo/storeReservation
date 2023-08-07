package com.store.reservation.reservation.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationError {

    NO_SUCH_RESERVATION("해당 예약은 존재하지 않습니다."),
    APPROVAL_STATE_NOT_CHANGED("예약 승인 또는 거절을 해주세요"),
    INVALID_ARRIVAL_STATE_UPDATE("도착 여부를 알 수 없습니다."),
    ALREADY_RESERVED("이미 예약된 시간입니다.");

    private final String description;

}
