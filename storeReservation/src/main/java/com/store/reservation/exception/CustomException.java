package com.store.reservation.exception;

public abstract class CustomException extends Exception{

    private Enum<?> errorCode;
    public abstract String getDescription();
    public abstract String getErrorCode();

}
