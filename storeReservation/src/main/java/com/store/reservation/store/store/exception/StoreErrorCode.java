package com.store.reservation.store.store.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreErrorCode {
    ALREADY_REGISTERED_STORE_NAME("이미 가입된 매장입니다."),
    UPDATE_BEFORE_CREATE_STORE("먼저 매장 등록을 진행해주세요"),
    NO_SUCH_STORE("등록되지 않은 상점입니다.");

    private final String description;

}
