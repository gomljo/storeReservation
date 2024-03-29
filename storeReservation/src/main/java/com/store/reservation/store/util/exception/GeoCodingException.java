package com.store.reservation.store.util.exception;

import com.store.reservation.exception.CustomException;


public class GeoCodingException extends CustomException {
    private final GeoCodingError geoCodingError;
    private final String description;

    public GeoCodingException(GeoCodingError geoCodingError){
        this.geoCodingError = geoCodingError;
        this.description = geoCodingError.getDescription();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getErrorCode() {
        return this.geoCodingError.toString();
    }
}
