package com.store.reservation.image.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageClientErrorCode {

    CAN_NOT_GET_IMAGE_DATA("이미지 데이터를 꺼낼 수 없습니다."),

    INVALID_FILE_NAME("파일 명이 올바르지 않습니다."),
    ALREADY_DELETED("이미 삭제되었습니다."),
    INVALID_EXTENSION("허용하지 않는 파일 확장자입니다."),
    INVALID_SYMBOL("파일 이름에 허용되지 않는 문자가 존재합니다.");
    private final String description;

}
