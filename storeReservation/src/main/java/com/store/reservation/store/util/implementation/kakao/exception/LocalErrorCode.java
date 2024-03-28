package com.store.reservation.store.util.implementation.kakao.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LocalErrorCode {
    CANNOT_GET_API_RESPONSE("api 응답을 받을 수 없습니다."),
    EMPTY_RESPONSE("처리할 응답 값이 없습니다."),
    ;
    private final String description;

}
