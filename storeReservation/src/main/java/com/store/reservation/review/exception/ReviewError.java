package com.store.reservation.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReviewError {
    NO_SUCH_REVIEW("리뷰를 찾을 수 없습니다."),
    ILLEGAL_ACCESS("올바른 접근이 아닙니다."),
    ;

    private final String description;
}
