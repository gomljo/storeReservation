package com.store.reservation.image.verification.strategies;

import com.store.reservation.image.exception.ImageRuntimeException;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.repository.MemberInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.store.reservation.image.exception.ImageErrorCode.TARGET_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MemberVerification implements VerificationStrategy {

    private final MemberInformationRepository memberInformationRepository;

    @Override
    public boolean verify(Long memberId, Long verificationTargetId) {
        MemberInformation memberInformation = memberInformationRepository.findById(verificationTargetId).orElseThrow(() -> new ImageRuntimeException(TARGET_NOT_FOUND));
        return memberInformation.isSame(memberId);
    }
}
