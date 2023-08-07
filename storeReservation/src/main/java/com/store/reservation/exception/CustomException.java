package com.store.reservation.exception;

public abstract class CustomException extends RuntimeException{

    protected abstract String getDescription();
    protected abstract String getErrorCode();
}
