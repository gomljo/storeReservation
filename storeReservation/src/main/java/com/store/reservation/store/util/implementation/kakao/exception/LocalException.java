package com.store.reservation.store.util.implementation.kakao.exception;

import com.store.reservation.exception.CustomException;


public class LocalException extends CustomException {

    private final LocalErrorCode localErrorCode;

    public LocalException(LocalErrorCode localErrorCode) {
        this.localErrorCode = localErrorCode;
    }

    @Override
    public String getDescription() {
        return localErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return localErrorCode.name();
    }
}
