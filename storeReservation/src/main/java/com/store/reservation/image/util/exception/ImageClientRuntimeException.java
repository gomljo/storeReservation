package com.store.reservation.image.util.exception;

import com.store.reservation.exception.CustomRuntimeException;

public class ImageClientRuntimeException extends CustomRuntimeException {
    private final ImageClientErrorCode imageClientErrorCode;

    public ImageClientRuntimeException(ImageClientErrorCode imageClientErrorCode){
        this.imageClientErrorCode = imageClientErrorCode;
    }

    @Override
    public String getDescription() {
        return imageClientErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return imageClientErrorCode.name();
    }
}
