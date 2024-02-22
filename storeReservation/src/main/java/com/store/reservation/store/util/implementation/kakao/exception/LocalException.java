package com.store.reservation.store.util.implementation.kakao.exception;

import com.store.reservation.exception.CustomException;

public class LocalException extends CustomException {

    private final LocalErrorCode localErrorCode;

    public LocalException(LocalErrorCode localErrorCode){
        this.localErrorCode = localErrorCode;
    }

    @Override
    protected String getDescription() {
        return localErrorCode.getDescription();
    }

    @Override
    protected String getErrorCode() {
        return localErrorCode.name();
    }
}
