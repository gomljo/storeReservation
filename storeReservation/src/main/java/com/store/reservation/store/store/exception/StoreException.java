package com.store.reservation.store.store.exception;

import com.store.reservation.exception.CustomException;


public class StoreException extends CustomException {

    private final StoreErrorCode storeErrorCode;
    private final String description;
    public StoreException(StoreErrorCode storeErrorCode){
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
