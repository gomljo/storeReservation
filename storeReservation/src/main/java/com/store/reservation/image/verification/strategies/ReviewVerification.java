package com.store.reservation.image.verification.strategies;

import com.store.reservation.image.exception.ImageRuntimeException;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.review.domain.Review;
import com.store.reservation.review.repository.jpa.ReviewRepository;

import static com.store.reservation.image.exception.ImageErrorCode.TARGET_NOT_FOUND;


public class ReviewVerification implements VerificationStrategy {

    private final ReviewRepository reviewRepository;

    public ReviewVerification(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public boolean verify(Long memberId, Long verificationTargetId) {
        // url 변조, 악성 실행 스크립트 삽입 등으로 다른 사람의 리뷰의 사진을 올릴 수 있는 가능성을 고려하기 위해 검증
        Review review = reviewRepository.findById(verificationTargetId)
                .orElseThrow(() -> new ImageRuntimeException(TARGET_NOT_FOUND));
        MemberInformation memberInformation = review.getMemberInformation();
        return memberInformation.isSame(memberId);
    }
}
