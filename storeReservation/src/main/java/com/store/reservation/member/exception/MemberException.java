package com.store.reservation.member.exception;

import com.store.reservation.exception.CustomException;

public class MemberException extends CustomException {

    private final MemberError errorCode;
    private final String errorMessage;

    public MemberException(MemberError errorCode){
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
