package com.store.reservation.auth.refreshToken.exception;

import com.store.reservation.exception.CustomRuntimeException;

public class TokenException extends CustomRuntimeException {

    private final TokenErrorCode tokenErrorCode;

    public TokenException(TokenErrorCode tokenErrorCode){
        this.tokenErrorCode = tokenErrorCode;
    }

    @Override
    public String getDescription() {
        return tokenErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return tokenErrorCode.name();
    }
}
