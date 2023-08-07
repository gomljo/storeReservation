package com.store.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {

    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다.");

    private final String description;
}
