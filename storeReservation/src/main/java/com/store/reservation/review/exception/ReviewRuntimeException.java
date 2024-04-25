package com.store.reservation.review.exception;

import com.store.reservation.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRuntimeException extends CustomRuntimeException {

    private final ReviewError reviewErrorCode;

    @Override
    public String getDescription() {
        return reviewErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return reviewErrorCode.name();
    }
}
