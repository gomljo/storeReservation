package com.store.reservation.image.verification.strategies;

public interface VerificationStrategy {

    boolean verify(Long memberId, Long verificationTargetId);
}
