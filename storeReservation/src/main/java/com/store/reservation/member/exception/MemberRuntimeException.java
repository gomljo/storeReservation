package com.store.reservation.member.exception;

import com.store.reservation.exception.CustomRuntimeException;

public class MemberRuntimeException extends CustomRuntimeException {

    private final MemberError errorCode;
    private final String errorMessage;

    public MemberRuntimeException(MemberError errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    @Override
    protected String getDescription() {
        return this.errorMessage;
    }

    @Override
    protected String getErrorCode() {
        return this.errorCode.toString();
    }
}
