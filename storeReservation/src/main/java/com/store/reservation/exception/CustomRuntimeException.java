package com.store.reservation.exception;

public abstract class CustomRuntimeException extends RuntimeException{

    public abstract String getDescription();
    public abstract String getErrorCode();
}
