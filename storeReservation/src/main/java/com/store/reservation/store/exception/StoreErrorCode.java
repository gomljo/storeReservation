package com.store.reservation.store.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreErrorCode {
    ALREADY_REGISTERED_STORE_NAME("이미 가입된 매장입니다."),
    UPDATE_BEFORE_CREATE_STORE("먼저 매장 등록을 진행해주세요"),
    NO_SUCH_STORE("등록되지 않은 상점입니다."),
    ALREADY_REGISTERED("이미 매장 정보가 등록된 회원입니다."),
    STORE_NOT_FOUND("소유하신 매장 정보를 찾을 수 없습니다.")
    ;

    private final String description;

}
