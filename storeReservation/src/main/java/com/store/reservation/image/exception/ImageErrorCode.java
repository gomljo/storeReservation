package com.store.reservation.image.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageErrorCode {
    INVALID_ACCESS("부적절한 접근입니다."),
    TARGET_NOT_FOUND("이미지를 저장할 대상이 존재하지 않습니다."),
    NO_SUCH_IMAGE("이미지를 찾을 수 없습니다.");
    private final String description;

}
