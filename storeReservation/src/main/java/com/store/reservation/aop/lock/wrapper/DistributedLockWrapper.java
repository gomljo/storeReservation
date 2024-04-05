package com.store.reservation.aop.lock.wrapper;

import com.store.reservation.aop.lock.exception.DistributedLockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static com.store.reservation.aop.lock.exception.DistributedLockErrorCode.LOCK_ERROR;

public class DistributedLockWrapper implements AutoCloseable {
    private final RLock rLock;
    public DistributedLockWrapper(RLock rLock) {
        this.rLock = rLock;
    }

    public void tryLock(long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException, DistributedLockException {
        boolean isLocked = this.rLock.tryLock(waitTime, leaseTime, timeUnit);
        if (!isLocked) {
            throw new DistributedLockException(LOCK_ERROR);
        }
    }

    @Override
    public void close() {
        rLock.unlock();
    }
}
