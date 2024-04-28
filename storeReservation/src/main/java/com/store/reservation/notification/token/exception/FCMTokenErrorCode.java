package com.store.reservation.notification.token.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FCMTokenErrorCode {

    NO_SUCH_TOKEN("토큰을 찾을 수 없습니다."),
    NO_SUCH_DEVICE("기기를 식별할 수 없습니다."),
    ALREADY_EXISTS("해당 유저의 토큰 정보가 이미 존재합니다."),
    ;


    private final String description;
}
