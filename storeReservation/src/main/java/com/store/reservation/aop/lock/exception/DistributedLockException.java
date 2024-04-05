package com.store.reservation.aop.lock.exception;

import com.store.reservation.exception.CustomException;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DistributedLockException extends CustomException {

    private final DistributedLockErrorCode distributedLockErrorCode;

    @Override
    public String getDescription() {
        return distributedLockErrorCode.getDescription();
    }

    @Override
    public String getErrorCode() {
        return distributedLockErrorCode.name();
    }
}
