package com.store.reservation.aop.lock.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DistributedLockErrorCode {

    LOCK_ERROR("");

    private final String description;
}
