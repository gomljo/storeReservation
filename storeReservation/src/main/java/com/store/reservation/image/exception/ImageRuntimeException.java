package com.store.reservation.image.exception;

import com.store.reservation.exception.CustomRuntimeException;

public class ImageRuntimeException extends CustomRuntimeException {
    private final ImageErrorCode imageErrorCode;

    public ImageRuntimeException(ImageErrorCode imageErrorCode){
        this.imageErrorCode = imageErrorCode;
    }

    @Override

    public String getDescription() {
        return this.imageErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return this.imageErrorCode.name();
    }
}
