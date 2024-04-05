package com.store.reservation.exception;

public abstract class CustomRuntimeException extends RuntimeException{

    protected abstract String getDescription();
    protected abstract String getErrorCode();
}
