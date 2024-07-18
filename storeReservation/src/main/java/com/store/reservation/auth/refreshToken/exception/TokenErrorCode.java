package com.store.reservation.auth.refreshToken.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode {
    NO_SUCH_TOKEN("일치하는 재발급 토큰을 찾을 수 없습니다."),
    SUSPICIOUS_ACTION("의심스러운 행위가 감지되었습니다."),

    TOKEN_EXPIRED("토큰이 만료되었습니다."),
    EMPTY_TOKEN("토큰이 없습니다."),
    INVALID_PREFIX("헤더 값은 올바르지 않습니다."),
    INVALID_ACCESS("부적절한 접근입니다.");

    private final String description;
}
