package com.store.reservation.aop.lock.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DistributedLock {
    String key() default "";

    String group() default "";

    long waitTime() default 20L;

    long leaseTime() default 100L;
}
