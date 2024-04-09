package com.store.reservation.store.util.implementation.kakao.exception;

import com.store.reservation.exception.CustomRuntimeException;


public class LocalRuntimeException extends CustomRuntimeException {

    private final LocalErrorCode localErrorCode;

    public LocalRuntimeException(LocalErrorCode localErrorCode) {
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
