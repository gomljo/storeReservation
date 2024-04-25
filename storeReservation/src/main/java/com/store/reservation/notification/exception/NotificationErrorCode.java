package com.store.reservation.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationErrorCode {
    NO_SUCH_TOKEN("일치하는 토큰을 찾을 수 없습니다."),
    ;

    private final String description;
}
