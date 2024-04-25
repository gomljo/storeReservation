package com.store.reservation.notification.exception;

import com.store.reservation.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationException extends CustomRuntimeException {

    private final NotificationErrorCode notificationErrorCode;
    @Override
    public String getDescription() {
        return notificationErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return notificationErrorCode.name();
    }
}
