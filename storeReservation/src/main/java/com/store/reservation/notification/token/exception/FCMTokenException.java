package com.store.reservation.notification.token.exception;

import com.store.reservation.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FCMTokenException extends CustomRuntimeException {

    private final FCMTokenErrorCode fcmTokenErrorCode;

    @Override
    public String getDescription() {
        return fcmTokenErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return fcmTokenErrorCode.name();
    }
}
