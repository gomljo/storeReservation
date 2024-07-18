package com.store.reservation.image.verification.strategies;

import com.store.reservation.image.exception.ImageRuntimeException;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.repository.jpa.StoreRepository;

import static com.store.reservation.image.exception.ImageErrorCode.TARGET_NOT_FOUND;


public class StoreVerification implements VerificationStrategy {

    private final StoreRepository storeRepository;

    public StoreVerification(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public boolean verify(Long memberId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ImageRuntimeException(TARGET_NOT_FOUND));
        MemberInformation memberInformation = store.getMemberInformation();
        return memberInformation.isSame(memberId);
    }

}
