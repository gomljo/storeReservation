package com.store.reservation.aop.lock;

import com.store.reservation.aop.lock.annotation.DistributedLock;
import com.store.reservation.aop.lock.exception.DistributedLockException;
import com.store.reservation.aop.lock.wrapper.DistributedLockWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static com.store.reservation.aop.lock.exception.DistributedLockErrorCode.LOCK_ERROR;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private static final String LOCK_KEY = "Lock : ";
    private static final String DELIMITER = "-";
    private final RedissonClient redissonClient;

    @Around("@annotation(com.store.reservation.aop.lock.annotation.DistributedLock)")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String keyExpression = distributedLock.key();
        String key = getKey(keyExpression, joinPoint);
        String group = distributedLock.group();

        RLock lock = redissonClient.getLock(getLockKey(group, key));

        try (DistributedLockWrapper distributedLockWrapper = new DistributedLockWrapper(lock)) {
            distributedLockWrapper.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TIME_UNIT);
        } catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException.getMessage());
        } catch (DistributedLockException distributedLockException) {
            throw new RuntimeException(LOCK_ERROR.getDescription());
        }
        return joinPoint.proceed();
    }

    private static String getLockKey(String group, String key) {
        return LOCK_KEY + group + DELIMITER + key;
    }

    private String getKey(String expression, ProceedingJoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return new SpelExpressionParser().parseExpression(expression).getValue(context, String.class);
    }
}
