package com.store.reservation.store.store.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeoCodingError {

    INVALID_ROAD_NAME("도로명이 잘못되었습니다."),
    INVALID_URL("요청 URI가 올바르지 않습니다."),
    INVALID_RESULT("주소 API 요청 결과가 올바르지 않습니다.");

    private final String description;
}
