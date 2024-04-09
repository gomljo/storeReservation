package com.store.reservation.store.exception;

import com.store.reservation.exception.CustomRuntimeException;


public class StoreRuntimeException extends CustomRuntimeException {

    private final StoreErrorCode storeErrorCode;
    private final String description;
    public StoreRuntimeException(StoreErrorCode storeErrorCode){
        this.storeErrorCode = storeErrorCode;
        this.description = storeErrorCode.getDescription();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getErrorCode() {
        return this.storeErrorCode.toString();
    }
}
