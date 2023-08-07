package com.store.reservation.reservation.log.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationLogErrorCode {
    NO_SUCH_REVIEW("일치하는 리뷰가 없습니다");

    private final String description;
}
